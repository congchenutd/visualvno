package com.fujitsu.us.visualvno.model.commands;

import com.fujitsu.us.visualvno.model.ContainerModel;
import com.fujitsu.us.visualvno.model.ShapeBase;

/**
 * Add a child shape to a container shape
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class AddCommand extends org.eclipse.gef.commands.Command
{
    private ContainerModel _parent;
    private ShapeBase      _child;
    private final int      _index;

    public AddCommand(ContainerModel parent, ShapeBase child, int index)
    {
        _parent = parent;
        _child  = child;
        _index  = index;
    }
    
    public ContainerModel getParent() {
        return _parent;
    }
    
    @Override
    public boolean canExecute() {
        return _parent.canAdd(_child);
    }
    
    @Override
    public void execute() {
        _parent.addChild(_child, _index);
    }

    @Override
    public void redo() {
        _parent.addChild(_child, _index);
    }

    @Override
    public void undo() {
        _parent.removeChild(_child);
    }

}
