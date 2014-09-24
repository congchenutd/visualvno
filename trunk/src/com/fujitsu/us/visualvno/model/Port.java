package com.fujitsu.us.visualvno.model;

import java.io.Serializable;


/**
 * A Shape has 0-n ports
 * A Host always has 1 port
 * A port connects to 0-1 link
 * 
 * Not part of the model, 
 * as it doesn't show up in the property view or the editor
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class Port implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final int           _number;
    private final ShapeModel    _shape;
    private LinkModel           _link;
    
    public Port(ShapeModel shape, int number)
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
    
    public LinkModel getLink() {
        return _link;
    }
    
    public void setLink(LinkModel link)
    {
        _link = link;
        getShape().updateLink(link);  // allow shape to fire property change
    }
    
    public void removeLink(LinkModel link)
    {
        _link = null;
        getShape().updateLink(link);  // allow shape to fire property change
    }
    
    /**
     * Whether this Port is connected to that
     */
    public boolean connectsTo(Port that) {
        return getLink() != null && getLink().equals(that.getLink());
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
        
        if(!(obj instanceof Port))
            return false;
        
        Port that = (Port) obj;
        return getShape().equals(that.getShape()) && 
               getNumber() == that.getNumber();
    }
    
}
