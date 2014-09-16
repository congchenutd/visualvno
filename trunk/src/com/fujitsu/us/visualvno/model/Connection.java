package com.fujitsu.us.visualvno.model;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.draw2d.Graphics;

/**
 * A connection between two distinct shapes.
 */
public class Connection extends ModelBase
{
    public static final Integer SOLID_CONNECTION  = new Integer(Graphics.LINE_SOLID);
    public static final Integer DASHED_CONNECTION = new Integer(Graphics.LINE_DASH);

    public static final String LINESTYLE_PROP    = "LineStyle";
    
    private static final IPropertyDescriptor[] descriptors       = new IPropertyDescriptor[1];
    private static final String SOLID_STR   = "Solid";
    private static final String DASHED_STR  = "Dashed";

    private static final long serialVersionUID  = 1;

    private boolean isConnected;
    private int     lineStyle = Graphics.LINE_SOLID;
    private Shape   source;
    private Shape   target;

    static {
        descriptors[0] = new ComboBoxPropertyDescriptor(LINESTYLE_PROP, 
                                                        LINESTYLE_PROP,
                                                        new String[] {SOLID_STR, 
                                                                      DASHED_STR});
    }

    public Connection(Shape source, Shape target) {
        reconnect(source, target);
    }
    
    public Shape getSource() {
        return source;
    }

    public Shape getTarget() {
        return target;
    }
    
    public int getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(int lineStyle)
    {
        if(lineStyle != Graphics.LINE_DASH &&
           lineStyle != Graphics.LINE_SOLID)
            throw new IllegalArgumentException();

        this.lineStyle = lineStyle;
        firePropertyChange(LINESTYLE_PROP, null, new Integer(this.lineStyle));
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return descriptors;
    }

    @Override
    public Object getPropertyValue(Object id)
    {
        if(id.equals(LINESTYLE_PROP))
        {
            if(getLineStyle() == Graphics.LINE_DASH)
                return new Integer(1);
            return new Integer(0);
        }
        return super.getPropertyValue(id);
    }
    
    /**
     * Sets the lineStyle based on the String provided by the PropertySheet
     */
    @Override
    public void setPropertyValue(Object id, Object value)
    {
        if(id.equals(LINESTYLE_PROP))
            setLineStyle(new Integer(1).equals(value) ? Graphics.LINE_DASH 
                    : Graphics.LINE_SOLID);
        else
            super.setPropertyValue(id, value);
    }

    public void reconnect()
    {
        if(!isConnected)
        {
            source.addConnection(this);
            target.addConnection(this);
            isConnected = true;
        }
    }

    public void reconnect(Shape newSource, Shape newTarget)
    {
        if(newSource == null || newTarget == null || newSource == newTarget)
            throw new IllegalArgumentException();

        disconnect();
        this.source = newSource;
        this.target = newTarget;
        reconnect();
    }
    
    public void disconnect()
    {
        if(isConnected)
        {
            source.removeConnection(this);
            target.removeConnection(this);
            isConnected = false;
        }
    }
}
