package com.fujitsu.us.visualvno.model;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * The model for a host
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class HostModel extends ShapeBase
                       implements ILinkEnd
{
    private static final long  serialVersionUID = 1;
    
    public  static final String SMALL_IMAGE  = "icons/Host.png";
    public  static final String BIG_IMAGE    = "icons/Host.png";
    private static final Image  ICON         = createImage(SMALL_IMAGE);
    private static final int    DEFAULT_WIDTH  = 40;
    private static final RGB    DEFAULT_COLOR  = ColorConstants.lightGreen.getRGB();

    // property ids
    public static final String IP_PROP  = "HostModel.IP";
    public static final String MAC_PROP = "HostModel.MAC";
    
    // property descriptors
    protected static IPropertyDescriptor[] _descriptors;
    static
    {
        // copy ShapeModel's descriptors, then add its own
        _descriptors = new IPropertyDescriptor[ShapeBase._descriptors.length + 2];
        int i;
        for(i = 0; i < ShapeBase._descriptors.length; ++i)
            _descriptors[i] = ShapeBase._descriptors[i];
        _descriptors[i]   = new TextPropertyDescriptor(IP_PROP,  "IP");
        _descriptors[i+1] = new TextPropertyDescriptor(MAC_PROP, "MAC");
    }
    
    // property values
    private String _ip  = new String();
    private String _mac = new String();
    
    public HostModel()
    {
        setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_WIDTH));
        setColor(DEFAULT_COLOR);
    }
    
    public String getIP() {
        return _ip;
    }
    
    public void setIP(String ip)
    {
        if(ip == null)
            throw new IllegalArgumentException();
        
        _ip = ip;
        firePropertyChange(IP_PROP, null, ip);
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

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return _descriptors;
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
    
    @Override
    public Image getIcon() {
        return ICON;
    }

    @Override
    public String toString() {
        return "Host " + getName();
    }
}
