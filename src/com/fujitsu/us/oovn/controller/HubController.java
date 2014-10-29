package com.fujitsu.us.oovn.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openflow.protocol.OFPacketIn;
import org.openflow.protocol.OFPacketOut;
import org.openflow.protocol.OFPort;
import org.openflow.protocol.action.OFAction;
import org.openflow.protocol.action.OFActionOutput;


/**
 * Turning a switch into a hub, by simply flooding all incoming packets
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class HubController extends Controller
{
    public HubController(int port) throws IOException {
        super(port);
    }
    
    @Override
    protected void handlePacketIn(OFSwitch sw, OFPacketIn packetIn)
    {
        // make a packet out message
        OFPacketOut packetOut = new OFPacketOut();
        packetOut.setBufferId(packetIn.getBufferId());
        packetOut.setInPort(packetIn.getInPort());

        // set actions as a flood action
        OFActionOutput action = new OFActionOutput();
        action.setMaxLength((short) 0);
        action.setPort(OFPort.OFPP_FLOOD.getValue());
        List<OFAction> actions = new ArrayList<OFAction>();
        actions.add(action);
        packetOut.setActions(actions);
        packetOut.setActionsLength((short) OFActionOutput.MINIMUM_LENGTH);

        try {
            sw.getStream().write(packetOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args)
    {
        try
        {
            HubController controller = new HubController(6634);
            controller.run();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

}

