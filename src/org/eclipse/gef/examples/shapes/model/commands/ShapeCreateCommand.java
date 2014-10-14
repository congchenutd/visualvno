package org.eclipse.gef.examples.shapes.model.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.shapes.model.ContainerModel;
import org.eclipse.gef.examples.shapes.model.ShapeBase;

/**
 * Create a new shape
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class ShapeCreateCommand extends Command
{
    private final ContainerModel _parent;
    private final ShapeBase     _shape;
    private final Rectangle      _bounds;

    public ShapeCreateCommand(ShapeBase newShape, ContainerModel parent, Rectangle bounds)
    {
        _parent = parent;
        _shape  = newShape;
        _bounds = bounds;
        setLabel("Shape creation");
    }

    @Override
    public boolean canExecute() {
        return  _shape  != null && 
                _parent != null && 
                _bounds != null;
    }

    @Override
    public void execute()
    {
        _shape.setLocation(_bounds.getLocation());
        Dimension size = _bounds.getSize();
        if(size.width > 0 && size.height > 0)
            _shape.setSize(size);
        redo();
    }

    @Override
    public void redo() {
        _parent.addChild(_shape);
    }

    @Override
    public void undo() {
        _parent.removeChild(_shape);
    }

}