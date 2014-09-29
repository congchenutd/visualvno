package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.ContainerModel;
import com.fujitsu.us.visualvno.model.ShapeModel;

public class AddToContainerCommand extends Command
{
    private final ContainerModel  _parent;
    private final ShapeModel      _child;
    private final int             _index;

    public AddToContainerCommand(ContainerModel parent, 
                                 ShapeModel child, int index)
    {
        super("Add children to container");
        _parent = parent;
        _child  = child;
        _index  = index;
    }

    @Override
    public void execute()
    {
        if(_index < 0)
            _parent.addChild(_child);
        else
            _parent.addChild(_child, _index);
    }

    @Override
    public void redo()
    {
        if(_index < 0)
            _parent.addChild(_child);
        else
            _parent.addChild(_child, _index);
    }

    @Override
    public void undo() {
        _parent.removeChild(_child);
    }
}
