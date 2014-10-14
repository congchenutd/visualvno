package com.fujitsu.us.visualvno.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

/**
 * Base for shapes that can container other shapes,
 * such as DiagramModel and SwitchModel
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public abstract class ContainerModel extends ShapeBase
{
    private static final long  serialVersionUID   = 1;
    
    // property ids
    public static final String CHILD_ADDED_PROP   = "ContainerModel.ChildAdded";
    public static final String CHILD_REMOVED_PROP = "ContainerModel.ChildRemoved";

    // property
    private List<ShapeBase> _children = new ArrayList<ShapeBase>();
 
    /**
     * Whether a child can be added into this container
     * Some subclasses may limit the types of children
     * e.g., SwitchModel only allows PortModel 
     */
    public boolean canAdd(ShapeBase child) {
        return true;
    }
    
    public boolean addChild(ShapeBase child) {
        return addChild(child, _children.size());
    }
    
    public boolean addChild(ShapeBase child, int index)
    {
        if(child == null)
            return false;
        if(index < 0 || index > _children.size())
            index = _children.size();
            
        _children.add(index, child);
        firePropertyChange(CHILD_ADDED_PROP, index, child);
        return true;
    }
    
    public boolean removeChild(ShapeBase child)
    {
        if(child != null && _children.remove(child))
        {
            firePropertyChange(CHILD_REMOVED_PROP, null, child);
            return true;
        }
        return false;
    }
    
    public void clearChildren()
    {
        _children.clear();
        firePropertyChange(CHILD_REMOVED_PROP, null, null);
    }
    
    public List<ShapeBase> getChildren() {
        return _children;
    }

    @Override
    public Image getIcon() {
        return null;
    }
}
