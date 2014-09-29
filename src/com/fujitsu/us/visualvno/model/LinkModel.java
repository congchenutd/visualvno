package com.fujitsu.us.visualvno.model;

import org.eclipse.draw2d.Graphics;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

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

    private String      _name       = new String();
    private int         _lineStyle  = Graphics.LINE_SOLID;
    private PortModel   _sourcePort;
    private PortModel   _targetPort;
    private boolean     _isConnected;

    static
    {
        _descriptors = new IPropertyDescriptor[]
        {
            new TextPropertyDescriptor(NAME_PROP, "Name"),
        };
    }

    public LinkModel() {}
    
    public LinkModel(PortModel source, PortModel target) {
        reconnect(source, target);
    }
    
    public PortModel getSourcePort() {
        return _sourcePort;
    }
    
    public PortModel getTargetPort() {
        return _targetPort;
    }
    
    public ShapeModel getSourceShape() {
        return getSourcePort().getShape();
    }

    public ShapeModel getTargetShape() {
        return getTargetPort().getShape();
    }
    
    public ShapeModel getTheOtherEnd(ShapeModel end) {
        return end.equals(_sourcePort) ? getTargetShape() 
                                       : end.equals(_targetPort) ? getSourceShape() 
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
            _sourcePort.setLink(this);
            _targetPort.setLink(this);
            _isConnected = true;
        }
    }

    public void reconnect(PortModel sourcePort, PortModel targetPort)
    {
        if(sourcePort == null || 
           targetPort == null || 
           sourcePort.equals(targetPort))
            throw new IllegalArgumentException();

        disconnect();
        _sourcePort = sourcePort;
        _targetPort = targetPort;
        reconnect();
    }
    
    public void disconnect()
    {
        if(_isConnected)
        {
            _sourcePort.removeLink(this);
            _targetPort.removeLink(this);
            _isConnected = false;
        }
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        
        if(!(obj instanceof LinkModel))
            return false;
        
        LinkModel that = (LinkModel) obj;
        return this.getSourcePort().equals(that.getSourcePort()) &&
               this.getTargetPort().equals(that.getTargetPort());
    }

}
