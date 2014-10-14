package com.fujitsu.us.visualvno.model.commands;

import java.util.List;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.model.LinkBase;
import com.fujitsu.us.visualvno.model.ShapeBase;

/**
 * A command to delete a shape from its parent.
 */
public class ShapeDeleteCommand extends Command
{
    private final ShapeBase    _shape;
    private final DiagramModel _parent;
    private List<LinkBase>     _sourceLinks;
    private List<LinkBase>     _targetLinks;
    private boolean            _wasRemoved;

    public ShapeDeleteCommand(DiagramModel parent, ShapeBase toBeDeleted)
    {
        if(parent == null || toBeDeleted == null)
            throw new IllegalArgumentException();

        setLabel("Shape deletion");
        _parent  = parent;
        _shape   = toBeDeleted;
    }

    private void addLinks(List<LinkBase> links) {
        for(LinkBase link: links)
            link.reconnect();
    }
    
    private void removeLinks(List<LinkBase> links) {
        for(LinkBase link: links)
            link.disconnect();
    }

    @Override
    public boolean canExecute() {
        return true;
    }
    
    @Override
    public boolean canUndo() {
        return _wasRemoved;
    }

    @Override
    public void execute()
    {
        // backup for undo
        _sourceLinks = _shape.getSourceLinks();
        _targetLinks = _shape.getTargetLinks();
        
        redo();
    }
    
    @Override
    public void redo()
    {
        // remove the child and disconnect its connections
        _wasRemoved = _parent.removeChild(_shape);
        if(_wasRemoved)
        {
            removeLinks(_sourceLinks);
            removeLinks(_targetLinks);
        }
    }

    @Override
    public void undo()
    {
        // recover the child and reconnect its connections
        if(_parent.addChild(_shape))
        {
            addLinks(_sourceLinks);
            addLinks(_targetLinks);
        }
    }
}
