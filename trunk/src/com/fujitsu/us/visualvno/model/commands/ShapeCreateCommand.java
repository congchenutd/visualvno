package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.Shape;
import com.fujitsu.us.visualvno.model.Diagram;

/**
 * A command to add a Shape to a ShapeDiagram.
 */
public class ShapeCreateCommand extends Command
{

    /** The new shape. */
    private final Shape         newShape;
    
    /** ShapeDiagram to add to. */
    private final Diagram parent;
    
    /** The bounds of the new Shape. */
    private final Rectangle     bounds;

    public ShapeCreateCommand(Shape newShape, Diagram parent, Rectangle bounds)
    {
        this.newShape = newShape;
        this.parent   = parent;
        this.bounds   = bounds;
        setLabel("Shape creation");
    }

    @Override
    public boolean canExecute() {
        return newShape != null && 
               parent   != null && 
               bounds   != null;
    }

    @Override
    public void execute()
    {
        newShape.setLocation(bounds.getLocation());
        Dimension size = bounds.getSize();
        if(size.width > 0 && size.height > 0)
            newShape.setSize(size);
        redo();
    }

    @Override
    public void redo() {
        parent.addChild(newShape);
    }

    @Override
    public void undo() {
        parent.removeChild(newShape);
    }

}
