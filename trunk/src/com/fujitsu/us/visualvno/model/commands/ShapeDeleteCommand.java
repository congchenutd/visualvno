package com.fujitsu.us.visualvno.model.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.ContainerModel;
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
    private List<LinkBase>     _sourceLinks = new ArrayList<LinkBase>();
    private List<LinkBase>     _targetLinks = new ArrayList<LinkBase>();
    private boolean            _wasRemoved;

    public ShapeDeleteCommand(DiagramModel parent, ShapeBase toBeDeleted)
    {
        if(parent == null || toBeDeleted == null)
            throw new IllegalArgumentException();

        setLabel("Shape deletion");
        _parent  = parent;
        _shape   = toBeDeleted;
    }

    private void restoreLinks(ShapeBase shape)
    {
        for(LinkBase link: _sourceLinks)
            link.reconnect();
        for(LinkBase link: _targetLinks)
            link.reconnect();
        _sourceLinks.clear();
        _targetLinks.clear();
    }
    
    /**
     * Remove all the links of a shape
     */
    private void removeLinks(ShapeBase shape)
    {
        // recursively remove (and backup) the links of its children
        if(shape instanceof ContainerModel)
        {
            ContainerModel container = (ContainerModel) shape;
            for(ShapeBase child: container.getChildren())
                removeLinks(child);
        }
        
        _sourceLinks.addAll(shape.getSourceLinks());  // backup
        _targetLinks.addAll(shape.getTargetLinks());
        for(LinkBase link: _sourceLinks)
            link.disconnect();
        for(LinkBase link: _targetLinks)
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
    public void execute() {
        redo();
    }
    
    @Override
    public void redo()
    {
        // remove the child and disconnect its connections
        _wasRemoved = _parent.removeChild(_shape);
        if(_wasRemoved)
            removeLinks(_shape);
    }

    @Override
    public void undo()
    {
        // recover the child and reconnect its connections
        if(_parent.addChild(_shape))
            restoreLinks(_shape);
    }
}
