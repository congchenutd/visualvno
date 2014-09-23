package com.fujitsu.us.visualvno.model;

/**
 * A Shape has 0-n ports
 * A Host always has 1 port
 * A port connects to 0-1 link
 * 
 * Not part of the model, 
 * as it doesn't show up in the property view or the editor
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class Port
{
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
    
    public void setLink(LinkModel link) {
        _link = link;
    }
    
}
