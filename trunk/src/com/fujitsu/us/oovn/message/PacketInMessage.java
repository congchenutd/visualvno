package com.fujitsu.us.oovn.message;

import java.util.LinkedList;

import net.onrc.openvirtex.packet.Ethernet;
import net.onrc.openvirtex.packet.IPv4;

import org.openflow.protocol.OFFlowMod;
import org.openflow.protocol.OFMatch;
import org.openflow.protocol.OFPacketIn;
import org.openflow.protocol.OFPacketOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.address.PhysicalIPAddress;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.datapath.VirtualSwitch;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.element.port.VirtualPort;
import com.fujitsu.us.oovn.map.MapBase;

public class PacketInMessage extends OFPacketIn implements Virtualizable
{
    Logger logger = LoggerFactory.getLogger(PacketInMessage.class);
    private PhysicalPort pPort = null;
    private VirtualPort  vPort = null;
    private VNO          vno   = null;

    @Override
    public void virtualize(final PhysicalSwitch sw)
    {

        VirtualSwitch vSwitch = MessageUtil.untranslateXid(this, sw);
        /*
         * Fetching port from the physical switch
         */

        short inport = getInPort();
        pPort = sw.getPort(inport);
        MapBase map = vno.getMap();

        final OFMatch match = new OFMatch();
        match.loadFromPacket(getPacketData(), inport);
        /*
         * Check whether this packet arrived on an edge port.
         *
         * if it did we do not need to rewrite anything, but just find which
         * controller this should be send to.
         */
        if (pPort.isEdge())
        {
            vno = fetchVNO(match, map);
            if(vno == null)
            {
                logger.warn(
                        "PacketIn {} does not belong to any virtual network; "
                                + "dropping and installing a temporary drop rule",
                        this);
                installDropRule(sw, match);
                return;
            }

            /*
             * Checks on vSwitch and the virtual port done in swndPkt.
             */
            vSwitch = fetchVirtualSwitch(sw, vSwitch, map);
            vPort = pPort.getVirtualPort(vno, 0);
            sendPkt(vSwitch, match, sw);
            logger.debug("Edge PacketIn {} sent to vno {}", this, vno.getID());
            return;
        }

        /*
         * Below handles packets traveling in the core.
         *
         *
         * The idea is to rewrite the packets such that the controller is
         * able to recognize them.
         *
         * For IPv4 packets and ARP packets this means rewriting the IP fields
         * and possibly the mac address fields if these packets are at the
         * egress point of a virtual link.
         */

        if(match.getDataLayerType() == Ethernet.TYPE_IPV4 || 
           match.getDataLayerType() == Ethernet.TYPE_ARP)
        {
            PhysicalIPAddress srcIP = new PhysicalIPAddress(match.getNetworkSource());
            PhysicalIPAddress dstIP = new PhysicalIPAddress(match.getNetworkDestination());

            Ethernet eth = new Ethernet();
            eth.deserialize(getPacketData(), 0, getPacketData().length);

            OVXLinkUtils lUtils = new OVXLinkUtils(eth.getSourceMAC(), eth.getDestinationMAC());
            
            // rewrite the OFMatch with the values of the link
            if (lUtils.isValid()) {
                OVXPort srcPort = port.getOVXPort(lUtils.getTenantId(),
                        lUtils.getLinkId());
                if (srcPort == null) {
                    logger.error(
                            "Virtual Src Port Unknown: {}, port {} with this match {}; dropping packet",
                            sw.getName(), match.getInputPort(), match);
                    return;
                }
                this.setInPort(srcPort.getPortNumber());
                OVXLink link;
                try {
                    OVXPort dstPort = map.getVirtualNetwork(
                            lUtils.getTenantId()).getNeighborPort(srcPort);
                    link = map.getVirtualSwitch(sw, lUtils.getTenantId())
                            .getMap().getVirtualNetwork(lUtils.getTenantId())
                            .getLink(dstPort, srcPort);
                } catch (SwitchMappingException | NetworkMappingException e) {
                    return; // same as (link == null)
                }
                this.ovxPort = this.port.getOVXPort(lUtils.getTenantId(),
                        link.getLinkId());
                OVXLinkField linkField = OpenVirteXController.getInstance()
                        .getOvxLinkField();
                // TODO: Need to check that the values in linkId and flowId
                // don't exceed their space
                if (linkField == OVXLinkField.MAC_ADDRESS) {
                    try {
                        LinkedList<MACAddress> macList = sw.getMap()
                                .getVirtualNetwork(this.ovxPort.getTenantId())
                                .getFlowManager()
                                .getFlowValues(lUtils.getFlowId());
                        eth.setSourceMACAddress(macList.get(0).toBytes())
                                .setDestinationMACAddress(
                                        macList.get(1).toBytes());
                        match.setDataLayerSource(eth.getSourceMACAddress())
                                .setDataLayerDestination(
                                        eth.getDestinationMACAddress());
                    } catch (NetworkMappingException e) {
                        logger.warn(e);
                    }
                } else if (linkField == OVXLinkField.VLAN) {
                    // TODO
                    logger.warn("VLAN virtual links not yet implemented.");
                    return;
                }

            }

            if (match.getDataLayerType() == Ethernet.TYPE_IPV4) {
                try {
                    final IPv4 ip = (IPv4) eth.getPayload();
                    ip.setDestinationAddress(map.getVirtualIP(dstIP).getIp());
                    ip.setSourceAddress(map.getVirtualIP(srcIP).getIp());
                    // TODO: Incorporate below into fetchTenantId
                    if (this.tenantId == null) {
                        this.tenantId = dstIP.getTenantId();
                    }
                } catch (AddressMappingException e) {
                    logger.warn("Could not rewrite IP fields : {}", e);
                }
            } else {
                logger.info("{} handling not yet implemented; dropping",
                        match.getDataLayerType());
                this.installDropRule(sw, match);
                return;
            }
            this.setPacketData(eth.serialize());

            vSwitch = this.fetchVirtualSwitch(sw, vSwitch, map);

            this.sendPkt(vSwitch, match, sw);
            logger.debug("IPv4 PacketIn {} sent to virtual network {}", this, vno);
            return;
        }

        this.tenantId = this.fetchVNO(match, map, true);
        if (tenantId == null) {
            logger.warn(
                    "PacketIn {} does not belong to any virtual network; "
                            + "dropping and installing a temporary drop rule",
                    this);
            this.installDropRule(sw, match);
            return;
        }
        vSwitch = this.fetchVirtualSwitch(sw, vSwitch, map);
        sendPkt(vSwitch, match, sw);
        logger.debug("Layer2 PacketIn {} sent to virtual network {}", this, vno);
    }

    private void sendPkt(final VirtualSwitch vSwitch, final OFMatch match,
            final PhysicalSwitch sw) {
        if (vSwitch == null)
        {
            logger.warn(
                    "Controller for virtual network {} has not yet connected "
                            + "or is down", vno.getID());
            installDropRule(sw, match);
            return;
        }
//        this.setBufferId(vSwitch.addToBufferMap(this));
        if (pPort != null && vPort != null)
        {
            setInPort((short) vPort.getNumber());
//            vSwitch.sendMsg(this, sw);
        } else if (pPort == null) {
            logger.error("The port {} doesn't belong to the physical switch {}",
                    getInPort(), sw.getName());
        } else if (vPort == null) {
            logger.error(
                    "Virtual port associated to physical port {} in physical switch {} for "
                            + "virtual network {} is not defined or inactive",
                    getInPort(), sw.getName(), vno.getID());
        }
    }

    // FIXME: not needed
//    private void learnAddresses(final OFMatch match, final MapBase map)
//    {
//        if(match.getDataLayerType() == Ethernet.TYPE_IPV4 ||
//           match.getDataLayerType() == Ethernet.TYPE_ARP)
//        {
//            if(!match.getWildcardObj().isWildcarded(Flag.NW_SRC))
//                map.getPhysicalIPAddress(
//                    new VirtualIPAddress(vno, match.getNetworkSource()));
//            if(!match.getWildcardObj().isWildcarded(Flag.NW_DST))
//                map.getPhysicalIPAddress(
//                    new VirtualIPAddress(vno, match.getNetworkDestination()));
//        }
//    }

    private void installDropRule(final PhysicalSwitch sw, final OFMatch match) {
        final OFFlowMod fm = new OFFlowMod();
        fm.setMatch(match);
        fm.setBufferId(this.getBufferId());
        fm.setHardTimeout((short) 1);
//        sw.sendMsg(fm, sw);
    }

    private VNO fetchVNO(final OFMatch match, final MapBase map)
    {
        MACAddress mac = MACAddress.valueOf(match.getDataLayerSource());
        VNO vno = map.getVNOfromMAC(mac);
        if(vno == null)
            logger.warn("Tried to return non-mapped MAC address : {}", mac);
        return vno;
    }

    private VirtualSwitch fetchVirtualSwitch(PhysicalSwitch psw, 
                                             VirtualSwitch  vsw,
                                             MapBase map)
    {
        if(vsw == null)
        {
            try {
                vsw = map.getVirtualSwitch(psw, vno);
            }
            catch(Exception e) {
                logger.warn("Cannot fetch non-mapped VirtualSwitch: {}", e);
            }
        }
        return vsw;
    }

    public PacketInMessage(final PacketInMessage other)
    {
        bufferId    = other.bufferId;
        inPort      = other.inPort;
        length      = other.length;
        packetData  = other.packetData;
        reason      = other.reason;
        totalLength = other.totalLength;
        type        = other.type;
        version     = other.version;
        xid         = other.xid;
    }


    public PacketInMessage(final byte[] data, final short portNumber)
    {
        super();
        setInPort(portNumber);
        setBufferId(OFPacketOut.BUFFER_ID_NONE);
        setReason(OFPacketIn.OFPacketInReason.NO_MATCH);
        setPacketData(data);
        setTotalLength((short) (OFPacketIn.MINIMUM_LENGTH + getPacketData().length));
        setLengthU    (         OFPacketIn.MINIMUM_LENGTH + getPacketData().length);
    }
}
