package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.LinkModel;
import com.fujitsu.us.visualvno.model.Port;

/**
 * A command to reconnect a Link to a different start point or end point.
 */
public class LinkReconnectCommand extends Command
{

    private final LinkModel _link;
    private final Port      _oldSource;
    private final Port      _oldTarget;
    private       Port      _newSource;
    private       Port      _newTarget;

    public LinkReconnectCommand(LinkModel link)
    {
        if(link == null)
            throw new IllegalArgumentException();
        
        setLabel("Link reconnection");
        _link = link;
        _oldSource  = link.getSourcePort();
        _oldTarget  = link.getTargetPort();
    }

    @Override
    public boolean canExecute()
    {
        // check for existing connection
        if(_newSource != null)
            return _oldTarget.canReconnectTo(_newSource);
        else if(_newTarget != null)
            return _oldSource.canReconnectTo(_newTarget);
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
            _link.reconnect(_newSource, _oldTarget);
        else if(_newTarget != null)
            _link.reconnect(_oldSource, _newTarget);
        else
            throw new IllegalStateException("Must call setNewSource() or setNewTarget() before");
    }

    /**
     * Note: _newTarget will be set to null, 
     * because _newSource and _newTarget are exclusive
     */
    public void setNewSource(Port newSource)
    {
        if(newSource == null)
            throw new IllegalArgumentException();

        setLabel("Move link startpoint");
        _newSource = newSource;
        _newTarget = null;
    }

    /**
     * Note: _newSource will be set to null, 
     * because _newSource and _newTarget are exclusive
     */
    public void setNewTarget(Port newTarget)
    {
        if(newTarget == null)
            throw new IllegalArgumentException();

        setLabel("Move link endpoint");
        _newSource = null;
        _newTarget = newTarget;
    }

    @Override
    public void undo() {
        _link.reconnect(_oldSource, _oldTarget);
    }

}
