package com.fujitsu.us.visualvno.model;

import org.eclipse.draw2d.Graphics;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * Base class for all the links
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public abstract class LinkBase extends ModelBase
{
    private static final long  serialVersionUID  = 1;

    // property ids
    public static final String NAME_PROP  = "LinkModel.Name";
    public static final String STYLE_PROP = "LinkModel.LineStyle";
    public static final String WIDTH_PROP = "LinkModel.LineWidth";
    public static final String VNOID_PROP = "LinkModel.VNOID";
    
    // descriptors
    private static final IPropertyDescriptor[] _descriptors;
    static {
        _descriptors = new IPropertyDescriptor[] {
                new TextPropertyDescriptor(NAME_PROP,  "Name"),
                new TextPropertyDescriptor(VNOID_PROP, "VNO ID")
        };
    }
    
    // property values
    private String    _name = new String();
    private ILinkEnd  _source;
    private ILinkEnd  _target;
    private boolean   _isConnected;
    private int       _lineStyle = Graphics.LINE_SOLID;
    private int       _lineWidth = 2;

    public ILinkEnd getSource() {
        return _source;
    }

    public ILinkEnd getTarget() {
        return _target;
    }
    
    /**
     * Get the other end of the link
     * @param oneEnd    one of the end
     * @return          the other end, or null if oneEnd is not a end of this link
     */
    public ILinkEnd getTheOtherEnd(ILinkEnd oneEnd) {
        return oneEnd == _source ? _target 
                                 : oneEnd == _target ? _source 
                                                     : null;
    }

    /**
     * Reconnect this connection to previous ends
     */
    public void reconnect()
    {
        if(!_isConnected)
        {
            _source.addLink(this);
            _target.addLink(this);
            _isConnected = true;
        }
    }

    public void reconnect(ILinkEnd newSource, ILinkEnd newTarget)
    {
        if(newSource == null || newTarget == null || newSource == newTarget)
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
            _source.removeLink(this);
            _target.removeLink(this);
            _isConnected = false;
        }
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
    
    public int getLineStyle() {
        return _lineStyle;
    }
    
    public void setLineStyle(int style)
    {
        _lineStyle = style;
        firePropertyChange(STYLE_PROP, null, style);
    }
    
    public int getLineWidth() {
        return _lineWidth;
    }
    
    public void setLineWidth(int width)
    {
        _lineWidth = width;
        firePropertyChange(WIDTH_PROP, null, width);
    }
    
    /**
     * Whether two ends can be connected by a link
     */
    public boolean canConnect(ILinkEnd source, ILinkEnd target)
    {
        if(source == null || target == null || 
           source.equals(target) ||    // self-self link
           source.connectsTo(target))  // already connected
            return false;

        // two ports of the same switch
        if(source instanceof PortModel && target instanceof PortModel)
        {
            PortModel sourcePort = (PortModel) source;
            PortModel targetPort = (PortModel) target;
            if(sourcePort.getShape() == targetPort.getShape() && 
               sourcePort.getShape() != null)
                return false;
        }
        return true;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return _descriptors;
    }

    @Override
    public Object getPropertyValue(Object id)
    {
        if(NAME_PROP.equals(id))
            return getName();
        if(VNOID_PROP.equals(id))
            return Integer.toString(getVNOID());
        return super.getPropertyValue(id);
    }
    
    @Override
    public void setPropertyValue(Object id, Object value)
    {
        if(NAME_PROP.equals(id))
            setName((String) value);
        else if(VNOID_PROP.equals(id))
            setVNOID(Integer.parseInt((String) value));
        else
            super.setPropertyValue(id, value);
    }

}