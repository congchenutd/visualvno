package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.LinkModel;
import com.fujitsu.us.visualvno.model.ShapeModel;

/**
 * A command to reconnect a Link to a different start point or end point.
 */
public class LinkReconnectCommand extends Command
{

    private final LinkModel   _connection;
    private final ShapeModel        _oldSource;
    private final ShapeModel        _oldTarget;
    private       ShapeModel        _newSource;
    private       ShapeModel        _newTarget;

    public LinkReconnectCommand(LinkModel connection)
    {
        if(connection == null)
            throw new IllegalArgumentException();
        
        _connection = connection;
        _oldSource  = connection.getSource();
        _oldTarget  = connection.getTarget();
    }

    @Override
    public boolean canExecute()
    {
        // check for existing connection
        if(_newSource != null)
            return !_newSource.connectsTo(_oldTarget);
        else if(_newTarget != null)
            return !_newTarget.connectsTo(_oldSource);
        return false;
    }

    /**
     * Reconnect the connection to newSource 
     * Must call setNewSource() or setNewTarget() before
     */
    @Override
    public void execute()
    {
        if(_newSource != null)
            _connection.reconnect(_newSource, _oldTarget);
        else if(_newTarget != null)
            _connection.reconnect(_oldSource, _newTarget);
        else
            throw new IllegalStateException("Must call setNewSource() or setNewTarget() before");
    }

    /**
     * Note: _newTarget will be set to null, 
     * because _newSource and _newTarget are exclusive
     */
    public void setNewSource(ShapeModel newSource)
    {
        if(newSource == null)
            throw new IllegalArgumentException();

        setLabel("Move connection startpoint");
        _newSource = newSource;
        _newTarget = null;
    }

    /**
     * Note: _newSource will be set to null, 
     * because _newSource and _newTarget are exclusive
     */
    public void setNewTarget(ShapeModel newTarget)
    {
        if(newTarget == null)
            throw new IllegalArgumentException();

        setLabel("Move connection endpoint");
        _newSource = null;
        _newTarget = newTarget;
    }

    @Override
    public void undo() {
        _connection.reconnect(_oldSource, _oldTarget);
    }

}
