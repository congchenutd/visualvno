package com.fujitsu.us.visualvno.model.commands;

import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.Connection;
import com.fujitsu.us.visualvno.model.Shape;
import com.fujitsu.us.visualvno.model.Diagram;

/**
 * A command to remove a shape from its parent.
 */
public class ShapeDeleteCommand extends Command
{
    /** Shape to remove. */
    private final Shape         child;

    /** ShapeDiagram to remove from. */
    private final Diagram parent;
    
    /** Holds a copy of the outgoing connections of child. */
    private List<Connection>    sourceConnections;
    
    /** Holds a copy of the incoming connections of child. */
    private List<Connection>    targetConnections;
    
    /** True, if child was removed from its parent. */
    private boolean             wasRemoved;

    public ShapeDeleteCommand(Diagram parent, Shape child)
    {
        if(parent == null || child == null)
            throw new IllegalArgumentException();

        setLabel("Shape deletion");
        this.parent = parent;
        this.child  = child;
    }

    private void addConnections(List<Connection> connections) {
        for(Iterator<Connection> iter = connections.iterator(); iter.hasNext();)
            iter.next().reconnect();
    }
    
    private void removeConnections(List<Connection> connections) {
        for(Iterator<Connection> iter = connections.iterator(); iter.hasNext();)
            iter.next().disconnect();
    }

    @Override
    public boolean canUndo() {
        return wasRemoved;
    }

    @Override
    public void execute()
    {
        // backup for undo
        sourceConnections = child.getSourceConnections();
        targetConnections = child.getTargetConnections();
        
        redo();
    }

    @Override
    public void redo()
    {
        // remove the child and disconnect its connections
        wasRemoved = parent.removeChild(child);
        if(wasRemoved)
        {
            removeConnections(sourceConnections);
            removeConnections(targetConnections);
        }
    }

    @Override
    public void undo()
    {
        // add the child and reconnect its connections
        if(parent.addChild(child))
        {
            addConnections(sourceConnections);
            addConnections(targetConnections);
        }
    }
}
