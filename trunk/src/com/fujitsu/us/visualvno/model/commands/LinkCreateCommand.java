package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.LinkModel;
import com.fujitsu.us.visualvno.model.ShapeModel;

/**
 * A command to create a Link between two shapes.
 */
public class LinkCreateCommand extends Command
{
    private LinkModel     _connection;
    private final int           _lineStyle;
    private final ShapeModel    _source;
    private ShapeModel          _target;

    public LinkCreateCommand(ShapeModel source, int lineStyle)
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
        _connection = new LinkModel(_source, _target);
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
