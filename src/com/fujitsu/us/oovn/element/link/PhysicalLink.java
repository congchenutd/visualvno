package com.fujitsu.us.oovn.element.link;

import com.fujitsu.us.oovn.element.Neo4jable;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PhysicalLink extends Link<PhysicalSwitch, PhysicalPort> implements Neo4jable
{
    public PhysicalLink(String name, PhysicalPort src, PhysicalPort dst) {
        super(name, src, dst);
    }
    
    @Override
    public String toDBMatch() {
        return "(" + toDBVariable() + 
                ":ZPhysical:Link " + "{" +
                    "type:\"PhysicalLink\"," +
                    "name:" + "\"" + getName() + "\", " + 
                    "srcSwitch:" + "\"" + getSrcSwitch().getDPID().toString() + "\", " +
                    "srcPort:" + getSrcPort().getNumber() + "," +
                    "dstSwitch:" + "\"" + getDstSwitch().getDPID().toString() + "\", " +
                    "dstPort:" + getDstPort().getNumber() +
                "})";
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = (JsonObject) super.toJson();
        result.addProperty("type", "PhysicalLink");
        return result;
    }

}