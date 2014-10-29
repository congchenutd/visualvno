package com.fujitsu.us.visualvno.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import com.fujitsu.us.oovn.controller.LearningSwitchController;
import com.fujitsu.us.oovn.element.address.IPAddress;
import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.model.Network;
import com.fujitsu.us.visualvno.ui.VNOEditor;

public class Demo
{
    private static Demo _instance;
    public Network _network1;
    public Network _network2;
    private LearningSwitchController _controller;
    private Thread _thread;
    
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
    
    private Demo()
    {
        try {
            _controller = new LearningSwitchController(6633);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        _thread = new Thread(_controller);
        _thread.start();
        
        _controller.addToGroup(new IPAddress("10.0.0.1"), 1);
        _controller.addToGroup(new IPAddress("10.0.0.2"), 1);
        _controller.addToGroup(new IPAddress("10.0.0.3"), 1);
        _controller.addToGroup(new IPAddress("10.0.0.4"), 2);
        _controller.addToGroup(new IPAddress("10.0.0.5"), 2);
    }
    
    public void init()
    {
//        ActionBase.VERIFY       .setEnabled(true);
//        ActionBase.START        .setEnabled(false);
//        ActionBase.STOP         .setEnabled(false);
//        ActionBase.DECOMMISSION .setEnabled(false);
    }
    
    public void verify(int vnoID)
    {
//        ActionBase.VERIFY       .setEnabled(false);
//        ActionBase.START        .setEnabled(true);
//        ActionBase.STOP         .setEnabled(false);
//        ActionBase.DECOMMISSION .setEnabled(false);
    }
    
    public void start(int vnoID)
    {
        _controller.start(vnoID);
//        ActionBase.VERIFY       .setEnabled(false);
//        ActionBase.START        .setEnabled(false);
//        ActionBase.STOP         .setEnabled(true);
//        ActionBase.DECOMMISSION .setEnabled(true);
    }
    
    public void stop(int vnoID)
    {
        _controller.stop(vnoID);
//        ActionBase.VERIFY       .setEnabled(false);
//        ActionBase.START        .setEnabled(true);
//        ActionBase.STOP         .setEnabled(false);
//        ActionBase.DECOMMISSION .setEnabled(false);
    }
    
    public void decommission(int vnoID)
    {
        _controller.stop(vnoID);
//        ActionBase.VERIFY       .setEnabled(false);
//        ActionBase.START        .setEnabled(false);
//        ActionBase.STOP         .setEnabled(false);
//        ActionBase.DECOMMISSION .setEnabled(false);
    }

}
