package com.fujitsu.us.visualvno.model;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * A property editor for location,
 * used by the Property view
 */
public class LocationPropertySource implements IPropertySource
{

    public static String ID_XPOS = "XPos";
    public static String ID_YPOS = "YPos";
    protected static IPropertyDescriptor[] _descriptors;

    static
    {
        PropertyDescriptor xProp = new TextPropertyDescriptor(ID_XPOS, "X");
        PropertyDescriptor yProp = new TextPropertyDescriptor(ID_YPOS, "Y");
        xProp.setValidator(new NumberValidator(0));
        yProp.setValidator(new NumberValidator(0));
        _descriptors = new IPropertyDescriptor[] { xProp, yProp };
    }

    protected Point _location = null;

    public LocationPropertySource(Point location) {
        _location = location.getCopy();
    }

    @Override
    public Object getEditableValue() {
        return _location.getCopy();
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return _descriptors;
    }

    @Override
    public Object getPropertyValue(Object propName)
    {
        if(ID_XPOS.equals(propName))
            return String.valueOf(_location.x());
        if(ID_YPOS.equals(propName))
            return String.valueOf(_location.y());
        return null;
    }

    @Override
    public boolean isPropertySet(Object propName) {
        return ID_XPOS.equals(propName) || 
               ID_YPOS.equals(propName);
    }

    @Override
    public void resetPropertyValue(Object propName) {}

    @Override
    public void setPropertyValue(Object propName, Object value)
    {
        if(ID_XPOS.equals(propName))
            _location.x = Integer.valueOf((String) value);
        else if(ID_YPOS.equals(propName))
            _location.y = Integer.valueOf((String) value);
    }

    @Override
    public String toString() {
        return new String("[" + _location.x() + ", " + _location.y() + "]");
    }

}
