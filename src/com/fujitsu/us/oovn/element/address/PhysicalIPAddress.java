package com.fujitsu.us.oovn.element.address;


public class PhysicalIPAddress extends IPAddress
{
    public PhysicalIPAddress(final String ipAddress) {
        super(ipAddress);
    }
    
    public PhysicalIPAddress(long ipValue) {
        super(ipValue);
    }

//    public Integer getTenantId() {
//        return ip >> (32 - OpenVirteXController.getInstance()
//                .getNumberVirtualNets());
//    }
}
