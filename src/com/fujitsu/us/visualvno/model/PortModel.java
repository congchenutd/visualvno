package com.fujitsu.us.visualvno.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


/**
 * A Shape has 0-n ports
 * A Host always has 1 port
 * A port connects to 0-1 link
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class PortModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final int        _number;
    private final ShapeModel _shape;
    private final List<LinkModel> _links = new LinkedList<LinkModel>();
    
    public PortModel(ShapeModel shape, int number)
    {
        _shape  = shape;
        _number = number;
    }
    
    public int getNumber() {
        return _number;
    }
    
    public ShapeModel getShape() {
        return _shape;
    }
    
    /**
     * @return  the first (and assumably the only) link
     */
    public LinkModel getLink() {
        return _links.isEmpty() ? null : _links.get(0);
    }
    
    /**
     * Set the given link as the only link
     */
    public void setLink(LinkModel link)
    {
        _links.clear();
        _links.add(link);
        getShape().updateLink(link);  // allow shape to fire property change
    }
    
    public void addLink(LinkModel link) {
        if(!_links.contains(link))
            _links.add(link);
    }
    
    public void removeLink(LinkModel link)
    {
        _links.remove(link);
        getShape().updateLink(link);  // allow shape to fire property change
    }
    
    public boolean canConnectTo(PortModel that) {
        return this.getLink() == null && canReconnectTo(that);
    }
    
    public boolean canReconnectTo(PortModel that) {
        return that != null &&
               that.getLink() == null &&
               this.getShape() != that.getShape();
    }
    
    @Override
    public String toString() {
        return getShape().toString() + ":" + getNumber();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        
        if(!(obj instanceof PortModel))
            return false;
        
        PortModel that = (PortModel) obj;
        return getShape().equals(that.getShape()) && 
               getNumber() == that.getNumber();
    }
    
}
