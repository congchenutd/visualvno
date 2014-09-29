package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.ContainerModel;
import com.fujitsu.us.visualvno.model.ShapeModel;

/**
 * A command to add a Shape to a ShapeDiagram.
 */
public class ShapeCreateCommand extends Command
{
    private final ShapeModel        _newShape;
    private final ContainerModel    _parent;
    private final Rectangle         _bounds;

    public ShapeCreateCommand(ShapeModel        newShape, 
                              ContainerModel    parent, 
                              Rectangle         bounds)
    {
        _newShape = newShape;
        _parent   = parent;
        _bounds   = bounds;
        setLabel("Shape creation");
    }

    @Override
    public boolean canExecute() {
        return _newShape != null && 
               _parent   != null && 
               _bounds   != null;
    }

    @Override
    public void execute()
    {
        _newShape.setLocation(_bounds.getLocation());
        Dimension size = _bounds.getSize();
        if(size.width > 0 && size.height > 0)
            _newShape.setSize(size);
        
        redo();
    }
    
    @Override
    public void redo() {
        _parent.addChild(_newShape);
    }

    @Override
    public void undo() {
        _parent.removeChild(_newShape);
    }

}
