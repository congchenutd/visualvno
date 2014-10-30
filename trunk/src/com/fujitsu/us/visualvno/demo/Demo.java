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

class ActionManager
{
	private enum State {UNINIT, INIT, VERIFY, START, STOP};
	private State _state = State.UNINIT;
	
	public boolean canInit() {
		return _state == State.UNINIT;
	}
	
	public boolean canVerify() {
		return _state == State.INIT;
	}
	
	public boolean canStart() {
		return _state == State.VERIFY || _state == State.STOP;
	}
	
	public boolean canStop() {
		return _state == State.START;
	}
	
	public boolean canDecommission() {
		return _state != State.UNINIT;
	}
	
	public void init() {
		_state = State.INIT;
	}
	
	public void verify() {
		_state = State.VERIFY;
	}
	
	public void start() {
		_state = State.START;
	}
	
	public void stop() {
		_state = State.STOP;
	}
	
	public void decommission() {
		_state = State.UNINIT;
	}
}

public class Demo
{
    private static Demo _instance;
    public Network[] _networks = new Network[2];
    private LearningSwitchController _controller;
    private Thread _thread;
    private ActionManager[] _actionManagers = new ActionManager[] {
    	new ActionManager(), new ActionManager() };
    
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
        _networks[0] = new Network(diagram, 1);
        _networks[1] = new Network(diagram, 2);
        diagram.removeNetwork(1);
        diagram.removeNetwork(2);
        editor.setDiagram(diagram);
    }
    
    public Network getNetwork(int vnoID) {
        return _networks[vnoID-1];
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
    
    public void init(int vnoID)
    {
    	_actionManagers[vnoID-1].init();
    }
    
    public void verify(int vnoID)
    {
    	_actionManagers[vnoID-1].verify();
    }
    
    public void start(int vnoID)
    {
    	_actionManagers[vnoID-1].start();
    	_controller.start(vnoID);
    }
    
    public void stop(int vnoID)
    {
    	_actionManagers[vnoID-1].stop();
    	_controller.stop(vnoID);
    }
    
    public void decommission(int vnoID)
    {
    	_actionManagers[vnoID-1].decommission();
    	_controller.stop(vnoID);
    }
    
    public boolean canInit(int vnoID) {
		return _actionManagers[vnoID-1].canInit();
	}
	
	public boolean canVerify(int vnoID) {
		return _actionManagers[vnoID-1].canVerify();
	}
	
	public boolean canStart(int vnoID) {
		return _actionManagers[vnoID-1].canStart();
	}
	
	public boolean canStop(int vnoID) {
		return _actionManagers[vnoID-1].canStop();
	}
	
	public boolean canDecommission(int vnoID) {
		return _actionManagers[vnoID-1].canDecommission();
	}
}
