package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.LinkModel;
import com.fujitsu.us.visualvno.model.PortModel;

/**
 * A command to create a Link between two shapes.
 */
public class LinkCreateCommand extends Command
{
    private LinkModel   _link;
    private final int   _lineStyle;
    private final PortModel  _sourcePort;
    private PortModel        _targetPort;

    public LinkCreateCommand(PortModel sourcePort, int lineStyle)
    {
        if(sourcePort == null)
            throw new IllegalArgumentException();

        setLabel("Link creation");
        _sourcePort = sourcePort;
        _lineStyle  = lineStyle;
    }
    
    public void setTarget(PortModel targetPort)
    {
        if(targetPort == null)
            throw new IllegalArgumentException();
        
        _targetPort = targetPort;
    }

    @Override
    public boolean canExecute() {
        return _sourcePort.canConnectTo(_targetPort);
    }

    @Override
    public void execute()
    {
        _link = new LinkModel(_sourcePort, _targetPort);
        _link.setLineStyle(_lineStyle);
    }

    @Override
    public void redo() {
        _link.reconnect();
    }

    @Override
    public void undo() {
        _link.disconnect();
    }
    
    public LinkModel getLink() {
    	return _link;
    }
}
