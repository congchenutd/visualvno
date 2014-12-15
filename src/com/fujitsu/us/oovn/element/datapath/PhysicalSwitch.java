package com.fujitsu.us.oovn.element.datapath;

import com.fujitsu.us.oovn.element.NetworkElement;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PhysicalSwitch extends Switch<PhysicalPort> implements NetworkElement
{
    public PhysicalSwitch(DPID dpid, String name) {
        super(dpid, name);
    }

    @Override
    public String toDBMatch() {
        return  "(" + toDBVariable() +
                ":ZPhysical:Switch {" +
                    "type:\"PhysicalSwitch\"," +
                    "dpid:\"" + getDPID().toString() + "\"," +
                    "name:\"" + getName() + 
                "\"})";
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = (JsonObject) super.toJson();
        result.addProperty("type", "PhysicalSwitch");
        return result;
    }
    
}
