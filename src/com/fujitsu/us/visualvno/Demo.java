package com.fujitsu.us.visualvno;

import java.io.InputStream;
import java.io.ObjectInputStream;

import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.model.Network;

public class Demo
{
    private static Demo _instance;
    
    public static Demo getInstance()
    {
        if(_instance == null)
            _instance = new Demo();
        return _instance;
    }
    
    private DiagramModel loadDiagram(String name)
    {
        ObjectInputStream in;
        try {
            InputStream stream = VisualVNOPlugin.class.getResourceAsStream(name);
            in = new ObjectInputStream(stream);
            return (DiagramModel) in.readObject(); 
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Network createPhysicalNetwork(int vnoID) {
        return new Network(loadDiagram("VNO" + vnoID + "Physical.vno"), 0);
    }

    public Network createVirtualNetwork(int vnoID) {
        return new Network(loadDiagram("VNO" + vnoID + "Virtual.vno"), vnoID);
    }
}
