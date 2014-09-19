package com.fujitsu.us.visualvno.model.commands;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.ConnectionModel;
import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.model.DiagramModel;

/**
 * A command to remove a shape from its parent.
 */
public class ShapeDeleteCommand extends Command
{
    private final ShapeModel        _toBeDeleted;
    private final DiagramModel      _parent;
    private List<ConnectionModel>   _sourceConnections;
    private List<ConnectionModel>   _targetConnections;
    private boolean                 _wasRemoved;

    public ShapeDeleteCommand(DiagramModel parent, ShapeModel toBeDeleted)
    {
        if(parent == null || toBeDeleted == null)
            throw new IllegalArgumentException();

        setLabel("Shape deletion");
        _parent         = parent;
        _toBeDeleted    = toBeDeleted;
    }

    private void addConnections(List<ConnectionModel> connections) {
        for(Iterator<ConnectionModel> iter = connections.iterator(); iter.hasNext();)
            iter.next().reconnect();
    }
    
    private void removeConnections(List<ConnectionModel> connections) {
        for(Iterator<ConnectionModel> iter = connections.iterator(); iter.hasNext();)
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
        _sourceConnections = _toBeDeleted.getSourceConnections();
        _targetConnections = _toBeDeleted.getTargetConnections();
        
        redo();
    }
    
    @Override
    public void redo()
    {
        // remove the child and disconnect its connections
        _wasRemoved = _parent.removeChild(_toBeDeleted);
        if(_wasRemoved)
        {
            removeConnections(_sourceConnections);
            removeConnections(_targetConnections);
        }
    }

    @Override
    public void undo()
    {
        // recover the child and reconnect its connections
        if(_parent.addChild(_toBeDeleted))
        {
            addConnections(_sourceConnections);
            addConnections(_targetConnections);
        }
    }
}
