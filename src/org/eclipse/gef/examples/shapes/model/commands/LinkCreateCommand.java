package org.eclipse.gef.examples.shapes.model.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.shapes.model.ILinkEnd;
import org.eclipse.gef.examples.shapes.model.LinkBase;

/**
 * A command to create a Link between two shapes.
 */
public class LinkCreateCommand extends Command
{
    private LinkBase        _link;
    private final ILinkEnd  _source;
    private ILinkEnd        _target;

    public LinkCreateCommand(Object link, ILinkEnd source)
    {
        if(source == null)
            throw new IllegalArgumentException();

        setLabel("Link creation");
        _link   = (LinkBase) link;
        _source = source;
    }
    
    public void setTarget(ILinkEnd target)
    {
        if(target == null)
            throw new IllegalArgumentException();
        
        _target = target;
    }

    @Override
    public boolean canExecute() {
        return _link.canConnect(_source, _target);
    }

    @Override
    public void execute() {
        _link.reconnect(_source, _target);
    }

    @Override
    public void redo() {
        _link.reconnect();
    }

    @Override
    public void undo() {
        _link.disconnect();
    }
    
    public LinkBase getLink() {
    	return _link;
    }
}
