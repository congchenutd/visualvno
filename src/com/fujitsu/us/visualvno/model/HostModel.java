package com.fujitsu.us.visualvno.model;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * A rectangular shape.
 */
public class HostModel extends ShapeModel
{
	private static final long serialVersionUID = 1L;
	public  static final String imageFileSmall  = "icons/rectangle16.gif";
    public  static final String imageFileBig    = "icons/rectangle24.gif";
    private static final Image  ICON            = createImage(imageFileSmall);

    protected static IPropertyDescriptor[] _descriptors;
    
    public static final String IP_PROP  = "Host.IP";
    public static final String MAC_PROP = "Host.MAC";
    
    static
    {
        // copy ShapeModel's descriptors, then add its own
        _descriptors = new IPropertyDescriptor[ShapeModel.descriptors.length + 2];
        int i;
        for(i = 0; i < ShapeModel.descriptors.length; ++i)
            _descriptors[i] = ShapeModel.descriptors[i];
        _descriptors[i]   = new TextPropertyDescriptor(IP_PROP,  "IP");
        _descriptors[i+1] = new TextPropertyDescriptor(MAC_PROP, "MAC");
    }
    
    private String _ip  = new String();
    private String _mac = new String();
    
    public HostModel() {
        setPortCount(1);
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
    public void setPortCount(int count) {
        super.setPortCount(1);
    }

}
