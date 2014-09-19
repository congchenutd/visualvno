package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.ConnectionModel;
import com.fujitsu.us.visualvno.model.ShapeModel;

/**
 * A command to create a connection between two shapes.
 */
public class ConnectionCreateCommand extends Command
{
    private ConnectionModel     _connection;
    private final int           _lineStyle;
    private final ShapeModel    _source;
    private ShapeModel          _target;

    public ConnectionCreateCommand(ShapeModel source, int lineStyle)
    {
        if(source == null)
            throw new IllegalArgumentException();

        setLabel("Connection creation");
        _source    = source;
        _lineStyle = lineStyle;
    }
    
    public void setTarget(ShapeModel target)
    {
        if(target == null)
            throw new IllegalArgumentException();
        
        _target = target;
    }

    @Override
    public boolean canExecute() {
        return !_source.connectsTo(_target);
    }

    @Override
    public void execute()
    {
        _connection = new ConnectionModel(_source, _target);
        _connection.setLineStyle(_lineStyle);
    }

    @Override
    public void redo() {
        _connection.reconnect();
    }

    @Override
    public void undo() {
        _connection.disconnect();
    }
}
