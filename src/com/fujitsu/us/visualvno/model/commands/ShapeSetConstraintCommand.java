package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import com.fujitsu.us.visualvno.model.Shape;

/**
 * A command to resize and/or move a shape.
 */
public class ShapeSetConstraintCommand extends Command
{
    private final Rectangle newBounds;
    private Rectangle       oldBounds;

	/** A request to move/resize an edit part. */
	private final ChangeBoundsRequest request;

	/** Shape to manipulate. */
	private final Shape shape;

	public ShapeSetConstraintCommand(Shape                 shape, 
	                                 ChangeBoundsRequest   req, 
	                                 Rectangle             newBounds)
	{
		if (shape == null || req == null || newBounds == null)
			throw new IllegalArgumentException();

		this.shape = shape;
		this.request = req;
		this.newBounds = newBounds.getCopy();
		setLabel("Move / Resize");
	}

	@Override
    public boolean canExecute()
	{
		Object type = request.getType();
		
		// make sure the Request is of a type we support:
		return (RequestConstants.REQ_MOVE           .equals(type) ||
		        RequestConstants.REQ_MOVE_CHILDREN  .equals(type) ||
		        RequestConstants.REQ_RESIZE         .equals(type) || 
		        RequestConstants.REQ_RESIZE_CHILDREN.equals(type));
	}

	@Override
    public void execute()
	{
	    // backup for undo
		oldBounds = new Rectangle(shape.getLocation(), 
		                          shape.getSize());
		redo();
	}

	@Override
    public void redo()
	{
		shape.setSize    (newBounds.getSize());
		shape.setLocation(newBounds.getLocation());
	}

	@Override
    public void undo()
	{
		shape.setSize    (oldBounds.getSize());
		shape.setLocation(oldBounds.getLocation());
	}
}
