package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.ContainerModel;
import com.fujitsu.us.visualvno.model.ShapeBase;

/**
 * Move a child out of a container
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 *
 */
public class OrphanChildCommand extends Command
{

    private Point          _oldLocation;
    private ContainerModel _parent;
    private ShapeBase     _child;

    public OrphanChildCommand(ContainerModel parent, ShapeBase child)
    {
        _parent = parent;
        _child  = child;
    }
    
    @Override
    public void execute()
    {
        _oldLocation = _child.getLocation();
        _parent.removeChild(_child);
    }

    @Override
    public void redo() {
        _parent.removeChild(_child);
    }

    @Override
    public void undo()
    {
        _child.setLocation(_oldLocation);
        _parent.addChild(_child);
    }

}
