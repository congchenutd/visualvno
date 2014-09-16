package com.fujitsu.us.visualvno.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A container for multiple shapes.
 * This is the root of the model data structure.
 */
public class ShapesDiagram extends ModelBase
{
	public static final String CHILD_ADDED_PROP   = "ShapesDiagram.ChildAdded";
	public static final String CHILD_REMOVED_PROP = "ShapesDiagram.ChildRemoved";
	private static final long serialVersionUID = 1;
	private final List<Shape> shapes = new ArrayList<Shape>();

    public boolean addChild(Shape shape)
    {
        if(shape != null && shapes.add(shape))
        {
            firePropertyChange(CHILD_ADDED_PROP, null, shape);
            return true;
        }
        return false;
    }

    public boolean removeChild(Shape shape)
    {
        if(shape != null && shapes.remove(shape))
        {
            firePropertyChange(CHILD_REMOVED_PROP, null, shape);
            return true;
        }
        return false;
    }
    
    /**
     * Return a List of Shapes in this diagram.
     * The returned List should not be modified.
     */
    public List<Shape> getChildren() {
        return shapes;
    }
}