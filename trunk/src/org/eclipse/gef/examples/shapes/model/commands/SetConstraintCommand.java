package org.eclipse.gef.examples.shapes.model.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.examples.shapes.model.ShapeBase;

/**
 * Set the size and location of a shape
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class SetConstraintCommand extends org.eclipse.gef.commands.Command
{
    private ShapeBase _shape;
    private Rectangle _newBound;
    private Rectangle _oldBound;

    public SetConstraintCommand(ShapeBase shape, Rectangle location)
    {
        setLabel("Set constraint");
        _shape    = shape;
        _newBound = location;
    }
    
    @Override
    public void execute()
    {
        _oldBound = new Rectangle(_shape.getLocation(), 
                                  _shape.getSize());
        redo();
    }

    @Override
    public void redo()
    {
        _shape.setSize    (_newBound.getSize());
        _shape.setLocation(_newBound.getLocation());
    }

    @Override
    public void undo()
    {
        _shape.setSize    (_oldBound.getSize());
        _shape.setLocation(_oldBound.getLocation());
    }

}
