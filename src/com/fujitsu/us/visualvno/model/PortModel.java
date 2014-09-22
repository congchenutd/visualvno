package com.fujitsu.us.visualvno.model;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class PortModel extends ShapeModel
{
    private static final long serialVersionUID = 1L;
    public  static final String imageFileSmall  = "icons/Port.png";
    public  static final String imageFileBig    = "icons/Port.png";
    private static final Image  ICON            = createImage(imageFileSmall);

    protected static IPropertyDescriptor[] _descriptors;
    
    public static final String NUMBER_PROP = "Port.Number";
    public static final String MAC_PROP    = "Port.MAC";
    
    private int    _number  = 1;
    private String _mac     = new String();
    
    static
    {
        _descriptors = new IPropertyDescriptor[] {
                new TextPropertyDescriptor(NUMBER_PROP, "Number"),
                new TextPropertyDescriptor(MAC_PROP,    "MAC"),
        };
    }
    
    @Override
    public Image getIcon() {
        return ICON;
    }
    
    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return _descriptors;
    }
    
    @Override
    public Object getPropertyValue(Object id)
    {
        if(NUMBER_PROP.equals(id))
            return getNumber();
        if(MAC_PROP.equals(id))
            return getMAC();
        return super.getPropertyValue(id);
    }
    
    @Override
    public void setPropertyValue(Object id, Object value)
    {
        if(NUMBER_PROP.equals(id))
            setNumber((Integer) value);
        if(MAC_PROP.equals(id))
            setMAC((String) value);
        else
            super.setPropertyValue(id, value);
    }
    
    public int getNumber() {
        return _number;
    }
    
    public void setNumber(int number)
    {
        if(number < 1)
            throw new IllegalArgumentException();
        
        _number = number;
        firePropertyChange(NUMBER_PROP, null, number);
    }
    
    public String getMAC() {
        return _mac;
    }
    
    public void setMAC(String mac)
    {
        if(mac == null)
            throw new IllegalArgumentException();
        
        _mac = mac;
        firePropertyChange(MAC_PROP, null, mac);
    }

}
