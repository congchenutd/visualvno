package com.fujitsu.us.visualvno.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

/**
 * A container for multiple shapes.
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public abstract class ContainerModel extends ShapeModel
{
    private static final long serialVersionUID = 1L;
    public static final String CHILD_ADDED_PROP   = "ContainerModel.ChildAdded";
    public static final String CHILD_REMOVED_PROP = "ContainerModel.ChildRemoved";
    
    private final List<ShapeModel> _children = new ArrayList<ShapeModel>();

    public boolean addChild(ShapeModel shape) {
        return addChild(shape, _children.size());
    }
    
    public boolean addChild(ShapeModel shape, int index)
    {
        if(shape == null || index < 0 || index > _children.size())
            return false;
            
        _children.add(index, shape);
        firePropertyChange(CHILD_ADDED_PROP, index, shape);
        return true;
    }

    public boolean removeChild(ShapeModel shape)
    {
        if(shape != null && _children.remove(shape))
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
        return _children;
    }
    
    @Override
    public Image getIcon() {
        return null;
    }
}
