package org.eclipse.gef.examples.shapes.model.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.shapes.model.ILinkEnd;
import org.eclipse.gef.examples.shapes.model.LinkBase;

/**
 * A command to reconnect a Link to a different start point or end point.
 */
public class LinkReconnectCommand extends Command
{
    private final LinkBase _link;
    private final ILinkEnd _oldSource;
    private final ILinkEnd _oldTarget;
    private       ILinkEnd _newSource;
    private       ILinkEnd _newTarget;

    public LinkReconnectCommand(LinkBase link)
    {
        if(link == null)
            throw new IllegalArgumentException();
        
        setLabel("Link reconnection");
        _link       = link;
        _oldSource  = link.getSource();
        _oldTarget  = link.getTarget();
    }

    @Override
    public boolean canExecute()
    {
        if(_newSource != null)
            return _link.canConnect(_newSource, _oldTarget);
        else if(_newTarget != null)
            return _link.canConnect(_oldSource, _newTarget);
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
    public void setNewSource(ILinkEnd source)
    {
        if(source == null)
            throw new IllegalArgumentException();
        
        setLabel("Move link startpoint");
        _newSource = source;
        _newTarget = null;
    }

    /**
     * Note: _newSource will be set to null, 
     * because _newSource and _newTarget are exclusive
     */
    public void setNewTarget(ILinkEnd target)
    {
        if(target == null)
            throw new IllegalArgumentException();

        setLabel("Move link endpoint");
        _newSource = null;
        _newTarget = target;
    }

    @Override
    public void undo() {
        _link.reconnect(_oldSource, _oldTarget);
    }

}