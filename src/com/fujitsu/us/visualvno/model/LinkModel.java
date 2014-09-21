package com.fujitsu.us.visualvno.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.eclipse.draw2d.Graphics;

/**
 * A Link between two distinct shapes.
 */
public class LinkModel extends ModelBase
{
	private static final long serialVersionUID = 1L;
    public static final String imageFileSmall = "icons/Link.png";
	public static final String imageFileBig   = "icons/Link.png";
    public static final Integer SOLID_CONNECTION  = new Integer(Graphics.LINE_SOLID);
    public static final Integer DASHED_CONNECTION = new Integer(Graphics.LINE_DASH);

    public static final String LINESTYLE_PROP   = "Connection.LineStyle";
    public static final String NAME_PROP        = "Connection.Name";
    
    private static final IPropertyDescriptor[] _descriptors;

    private String      _name = new String();
    private int         _lineStyle = Graphics.LINE_SOLID;
    private ShapeModel  _source;
    private ShapeModel  _target;
    private boolean     _isConnected;

    static
    {
        _descriptors = new IPropertyDescriptor[]
        {
            new TextPropertyDescriptor(NAME_PROP, "Name"),
        };
    }

    public LinkModel() {}
    
    public LinkModel(ShapeModel source, ShapeModel target) {
        reconnect(source, target);
    }
    
    public ShapeModel getSource() {
        return _source;
    }

    public ShapeModel getTarget() {
        return _target;
    }
    
    public ShapeModel getTheOtherEnd(ShapeModel end) {
        return end.equals(_source) ? getTarget() 
                                   : end.equals(_target) ? getSource() 
                                                         : null;
    }
    
    public int getLineStyle() {
        return _lineStyle;
    }

    public void setLineStyle(int lineStyle)
    {
        if(lineStyle != Graphics.LINE_DASH &&
           lineStyle != Graphics.LINE_SOLID)
            throw new IllegalArgumentException();

        _lineStyle = lineStyle;
        firePropertyChange(LINESTYLE_PROP, null, lineStyle);
    }
    
    public String getName() {
        return _name;
    }
    
    public void setName(String name)
    {
        if(name == null)
            throw new IllegalArgumentException();
        
        _name = name;
        firePropertyChange(NAME_PROP, null, name);
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return _descriptors;
    }

    @Override
    public Object getPropertyValue(Object id)
    {
        if(id.equals(LINESTYLE_PROP))
            return getLineStyle() == Graphics.LINE_DASH ? new Integer(1)
                                                        : new Integer(0);
        if(NAME_PROP.equals(id))
            return getName();
        return super.getPropertyValue(id);
    }
    
    @Override
    public void setPropertyValue(Object id, Object value)
    {
        if(id.equals(LINESTYLE_PROP))
            setLineStyle(new Integer(1).equals(value) ? Graphics.LINE_DASH 
                                                      : Graphics.LINE_SOLID);
        else if(NAME_PROP.equals(id))
            setName((String) value);
        else
            super.setPropertyValue(id, value);
    }

    public void reconnect()
    {
        if(!_isConnected)
        {
            _source.addConnection(this);
            _target.addConnection(this);
            _isConnected = true;
        }
    }

    public void reconnect(ShapeModel newSource, ShapeModel newTarget)
    {
        if(newSource == null || 
           newTarget == null || 
           newSource.equals(newTarget))
            throw new IllegalArgumentException();

        disconnect();
        _source = newSource;
        _target = newTarget;
        reconnect();
    }
    
    public void disconnect()
    {
        if(_isConnected)
        {
            _source.removeConnection(this);
            _target.removeConnection(this);
            _isConnected = false;
        }
    }
    
    @Override
    public String toString() {
        return getName();
    }
}
