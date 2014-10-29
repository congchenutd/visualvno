package com.fujitsu.us.visualvno;

import java.io.InputStream;
import java.io.ObjectInputStream;

import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.model.Network;
import com.fujitsu.us.visualvno.ui.VNOEditor;

public class Demo
{
    private static Demo _instance;
    public Network _network1;
    public Network _network2;
    
    public static Demo getInstance()
    {
        if(_instance == null)
            _instance = new Demo();
        return _instance;
    }
    
    public DiagramModel loadDiagram(String name)
    {
        name = "runtime/VNO/Base/" + name;
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
    
    public void loadGlobal(VNOEditor editor)
    {
        DiagramModel diagram = Demo.getInstance().loadDiagram("Global12.vno");
        _network1 = new Network(diagram, 1);
        _network2 = new Network(diagram, 2);
        diagram.removeNetwork(1);
        diagram.removeNetwork(2);
        editor.setDiagram(diagram);
    }
//    
//    public Network createPhysicalNetwork(int vnoID) {
//        return new Network(loadDiagram("runtime/VNO/Base/VNO" + vnoID + "Physical.vno"), 0);
//    }
//
//    public Network createVirtualNetwork(int vnoID) {
//        return new Network(loadDiagram("runtime/VNO/Base/VNO" + vnoID + "Virtual.vno"), vnoID);
//    }
//    
//    public Network createWholeNetwork(int vnoID) {
//        return new Network(loadDiagram("runtime/VNO/Base/VNO" + vnoID + "Whole.vno"), vnoID);
//    }

}
