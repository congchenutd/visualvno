package com.fujitsu.us.visualvno.model.commands;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.ContainerModel;
import com.fujitsu.us.visualvno.model.ShapeModel;

/**
 * Drag a child out of its container
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class OrphanChildCommand extends Command
{

    private final ContainerModel  _parent;
    private final ShapeModel      _child;
    private int             _index;         // for backup
    private Point           _oldLocation;   //

    public OrphanChildCommand(ContainerModel parent, ShapeModel child)
    {
        super("Orphan a child");
        _parent = parent;
        _child  = child;
    }

    @Override
    public void execute()
    {
        List<ShapeModel> children = _parent.getChildren();
        _index       = children.indexOf(_child);
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
        _parent.addChild(_child, _index);
    }

}
