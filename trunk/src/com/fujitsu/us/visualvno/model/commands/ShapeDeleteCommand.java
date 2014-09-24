package com.fujitsu.us.visualvno.model.commands;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.LinkModel;
import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.model.DiagramModel;

/**
 * A command to remove a shape from its parent.
 */
public class ShapeDeleteCommand extends Command
{
    private final ShapeModel    _shape;
    private final DiagramModel  _diagram;
    private List<LinkModel>     _sourceLinks;
    private List<LinkModel>     _targetLinks;
    private boolean             _wasRemoved;

    public ShapeDeleteCommand(DiagramModel parent, ShapeModel toBeDeleted)
    {
        if(parent == null || toBeDeleted == null)
            throw new IllegalArgumentException();

        setLabel("Shape deletion");
        _diagram  = parent;
        _shape    = toBeDeleted;
    }

    private void addConnections(List<LinkModel> connections) {
        for(Iterator<LinkModel> iter = connections.iterator(); iter.hasNext();)
            iter.next().reconnect();
    }
    
    private void removeConnections(List<LinkModel> connections) {
        for(Iterator<LinkModel> iter = connections.iterator(); iter.hasNext();)
            iter.next().disconnect();
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
        _wasRemoved = _diagram.removeChild(_shape);
        if(_wasRemoved)
        {
            removeConnections(_sourceLinks);
            removeConnections(_targetLinks);
        }
    }

    @Override
    public void undo()
    {
        // recover the child and reconnect its connections
        if(_diagram.addChild(_shape))
        {
            addConnections(_sourceLinks);
            addConnections(_targetLinks);
        }
    }
}
