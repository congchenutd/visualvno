package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import com.fujitsu.us.visualvno.model.ShapeModel;

/**
 * A command to resize/move a shape.
 */
public class ShapeSetConstraintCommand extends Command
{
	private final ChangeBoundsRequest  _request;
	private final ShapeModel           _shape;
	private final Rectangle            _newBounds;
	private Rectangle                  _oldBounds;

	public ShapeSetConstraintCommand(ShapeModel            shape, 
	                                 ChangeBoundsRequest   req, 
	                                 Rectangle             newBounds)
	{
		if (shape == null || req == null || newBounds == null)
			throw new IllegalArgumentException();

		setLabel("Move / Resize");
		_shape        = shape;
		_request      = req;
		_newBounds    = newBounds.getCopy();

		// ensure the shape is always a square or circle
		int diameter = Math.max(newBounds.width(), newBounds.height());
		_newBounds.setWidth (diameter);
		_newBounds.setHeight(diameter);
	}

	@Override
    public boolean canExecute()
	{
		Object type = _request.getType();
		
		// make sure the Request is of a type we support
		return (RequestConstants.REQ_MOVE           .equals(type) ||
		        RequestConstants.REQ_MOVE_CHILDREN  .equals(type) ||
		        RequestConstants.REQ_RESIZE         .equals(type) || 
		        RequestConstants.REQ_RESIZE_CHILDREN.equals(type));
	}

	@Override
    public void execute()
	{
	    // backup for undo
		_oldBounds = new Rectangle(_shape.getLocation(), 
		                           _shape.getSize());
		redo();
	}
	
	@Override
    public void redo()
	{
	    _shape.setSize    (_newBounds.getSize());
	    _shape.setLocation(_newBounds.getLocation());
	}

	@Override
    public void undo()
	{
		_shape.setSize    (_oldBounds.getSize());
		_shape.setLocation(_oldBounds.getLocation());
	}
}
