package org.eclipse.gef.examples.shapes.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * A dimension editor used by the Property view
 */
public class DimensionPropertySource implements IPropertySource
{

    public static String ID_WIDTH  = "width";
    public static String ID_HEIGHT = "height";
    private static int MIN_WIDTH = 10;
    protected static IPropertyDescriptor[] _descriptors;

    static
    {
        PropertyDescriptor widthProp  = new TextPropertyDescriptor(ID_WIDTH,  "Width");
        PropertyDescriptor heightProp = new TextPropertyDescriptor(ID_HEIGHT, "Height");
        widthProp .setValidator(new NumberValidator(MIN_WIDTH));
        heightProp.setValidator(new NumberValidator(MIN_WIDTH));
        _descriptors = new IPropertyDescriptor[] { widthProp, heightProp };
    }

    protected Dimension _dimension = null;

    public DimensionPropertySource(Dimension dimension) {
        _dimension = dimension.getCopy();
    }

    @Override
    public Object getEditableValue() {
        return _dimension.getCopy();
    }

    @Override
    public Object getPropertyValue(Object propName) {
        return getPropertyValue((String) propName);
    }

    public Object getPropertyValue(String propName)
    {
        if(ID_HEIGHT.equals(propName))
            return String.valueOf(_dimension.height());
        if(ID_WIDTH.equals(propName))
            return String.valueOf(_dimension.width());
        return null;
    }

    @Override
    public void setPropertyValue(Object propName, Object value) {
        setPropertyValue((String) propName, value);
    }

    public void setPropertyValue(String propName, Object value)
    {
        if(ID_HEIGHT.equals(propName))
            _dimension.height = Integer.valueOf((String) value);
        else if(ID_WIDTH.equals(propName))
            _dimension.width = Integer.valueOf((String) value);
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return _descriptors;
    }

    @Override
    public void resetPropertyValue(Object propName) {}

    @Override
    public boolean isPropertySet(Object propName) {
        return true;
    }

    public boolean isPropertySet(String propName) {
        return ID_HEIGHT.equals(propName) || 
               ID_WIDTH .equals(propName);
    }

    @Override
    public String toString() {
        return new String("(" + _dimension.width() + ", " + _dimension.height() + ")");
    }

}
