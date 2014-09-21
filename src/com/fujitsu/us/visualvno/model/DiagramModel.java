package com.fujitsu.us.visualvno.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A container for multiple shapes.
 * This is the root of the model data structure.
 */
public class DiagramModel extends ModelBase
{
	private static final long serialVersionUID = 1L;
	public static final String CHILD_ADDED_PROP   = "ShapesDiagram.ChildAdded";
	public static final String CHILD_REMOVED_PROP = "ShapesDiagram.ChildRemoved";
	
	private final List<ShapeModel> _shapes = new ArrayList<ShapeModel>();

    public boolean addChild(ShapeModel shape)
    {
        if(shape != null && _shapes.add(shape))
        {
            firePropertyChange(CHILD_ADDED_PROP, null, shape);
            return true;
        }
        return false;
    }

    public boolean removeChild(ShapeModel shape)
    {
        if(shape != null && _shapes.remove(shape))
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
    public List<ShapeModel> getChildren() {
        return _shapes;
    }
}