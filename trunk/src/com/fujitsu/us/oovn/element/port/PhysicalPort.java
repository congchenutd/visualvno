package com.fujitsu.us.oovn.element.port;

import com.fujitsu.us.oovn.element.Neo4jable;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PhysicalPort extends Port<PhysicalSwitch, PhysicalLink> 
                          implements Neo4jable
{
    public PhysicalPort(int number, MACAddress mac) {
        super(number, mac);
    }
    
    @Override
    public String toDBMatch() {
        return  "(" + toDBVariable() +
                ":ZPhysical:Port {" +
                    "type:\"PhysicalPort\"," +
                    "switch:\"" + getSwitch().getDPID().toString() + "\"," +
                    "number:" + getNumber() + "," +
                    "mac:\""  + getMACAddress() + "\"" +
                "})";
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = (JsonObject) super.toJson();
        result.addProperty("type", "PhysicalPort");
        return result;
    }

}
