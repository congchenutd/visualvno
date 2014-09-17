package com.fujitsu.us.visualvno.model;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * A rectangular shape.
 */
public class Host extends Shape
{
    public  static final String imageFileSmall = "icons/rectangle16.gif";
    public  static final String imageFileBig   = "icons/rectangle24.gif";
    private static final Image ICON = createImage(imageFileSmall);

    protected static IPropertyDescriptor[] descriptors;
    
    public static final String IP_PROP  = "Host.IP";
    public static final String MAC_PROP = "Host.MAC";
    
    static
    {
        Host.descriptors = new IPropertyDescriptor[Shape.descriptors.length + 2];
        int i;
        for(i = 0; i < Shape.descriptors.length; ++i)
            Host.descriptors[i] = Shape.descriptors[i];
        Host.descriptors[i]   = new TextPropertyDescriptor(IP_PROP,  "IP");
        Host.descriptors[i+1] = new TextPropertyDescriptor(MAC_PROP, "MAC");
    }
    
    private String ip  = new String();
    private String mac = new String();
    
    public String getIP() {
        return ip;
    }
    
    public void setIP(String ip)
    {
        this.ip = ip;
        firePropertyChange(IP_PROP, null, ip);
    }
    
    public String getMAC() {
        return mac;
    }
    
    public void setMAC(String mac)
    {
        this.mac = mac;
        firePropertyChange(MAC_PROP, null, mac);
    }
    
    @Override
    public Image getIcon() {
        return ICON;
    }
    
    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return Host.descriptors;
    }
    
    @Override
    public Object getPropertyValue(Object id)
    {
        if(IP_PROP.equals(id))
            return getIP();
        if(MAC_PROP.equals(id))
            return getMAC();
        return super.getPropertyValue(id);
    }
    
    @Override
    public void setPropertyValue(Object id, Object value)
    {
        if(IP_PROP.equals(id))
            setIP((String) value);
        if(MAC_PROP.equals(id))
            setMAC((String) value);
        else
            super.setPropertyValue(id, value);
    }

}
