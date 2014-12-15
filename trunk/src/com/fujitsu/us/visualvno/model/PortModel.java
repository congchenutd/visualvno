package com.fujitsu.us.visualvno.model;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * A Shape has 0-n ports
 * A Host behaves like a port
 * A port connects to 0-1 link
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class PortModel extends ShapeBase
                       implements ILinkEnd
{
    private static final long serialVersionUID = 1L;
    
    public  static final String SMALL_IMAGE  = "icons/Port.png";
    public  static final String BIG_IMAGE    = "icons/Port.png";
    private static final Image  ICON         = createImage(SMALL_IMAGE);
    private static final RGB    DEFAULT_COLOR = ColorConstants.white.getRGB();
    public  static final int    DEFAULT_WIDTH = 20;

    // property ids
    public static final String IP_PROP  = "PortModel.IP";
    public static final String MAC_PROP = "PortModel.MAC";
    
    // property descriptors
    protected static IPropertyDescriptor[] _descriptors;
    static
    {
        // copy ShapeModel's descriptors, then add its own
        _descriptors = new IPropertyDescriptor[ShapeBase._descriptors.length + 2];
        int i;
        for(i = 0; i < ShapeBase._descriptors.length; ++i)
        {
            _descriptors[i] = ShapeBase._descriptors[i];
            if(_descriptors[i].getId() == ShapeBase.NAME_PROP)
                ((PropertyDescriptor) _descriptors[i]).setValidator(new NumberValidator(1));
        }
        _descriptors[i]   = new TextPropertyDescriptor(IP_PROP,  "IP");
        _descriptors[i+1] = new TextPropertyDescriptor(MAC_PROP, "MAC");
    }
    
    // property values
    private ShapeBase _shape;
    private String    _ip  = new String();
    private String    _mac = new String();
    
    public PortModel(ShapeBase shape)
    {
        setShape(shape);
        setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_WIDTH));
        setColor(DEFAULT_COLOR);
    }
    
    public int getNumber() {
        return getName().isEmpty() ? 0: Integer.valueOf(getName());
    }
    
    public void setNumber(int number) {
        setName(String.valueOf(number));
    }
    
    public String getIPAddress() {
        return _ip;
    }
    
    public void setIPAddress(String ip)
    {
        _ip = ip;
        firePropertyChange(IP_PROP, null, ip);
    }
    
    public String getMAC() {
        return _mac;
    }
    
    public void setMAC(String mac)
    {
        _mac = mac;
        firePropertyChange(MAC_PROP, null, mac);
    }
    
    public ShapeBase getShape() {
        return _shape;
    }
    
    public void setShape(ShapeBase shape) {
        _shape = shape;
    }
    
    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return _descriptors;
    }

    @Override
    public Object getPropertyValue(Object id)
    {
        if(id.equals(IP_PROP))
            return getIPAddress();
        if(id.equals(MAC_PROP))
            return getMAC();
        return super.getPropertyValue(id);
    }
    
    @Override
    public void setPropertyValue(Object id, Object value)
    {
        if(id.equals(IP_PROP))
            setIPAddress((String) value);
        else if(id.equals(MAC_PROP))
            setMAC((String) value);
        else
            super.setPropertyValue(id, value);
    }
    
    @Override
    public String toString() {
        return getShape() == null ? "Port " + getNumber()
                                  : getShape().toString() + " :Port " + getNumber();
    }
    
//    @Override
//    public boolean equals(Object obj)
//    {
//        if(this == obj)
//            return true;
//        
//        if(!(obj instanceof PortModel))
//            return false;
//        
//        PortModel that = (PortModel) obj;
//        return getShape().equals(that.getShape()) && 
//               getNumber() == that.getNumber();
//    }

    @Override
    public Image getIcon() {
        return ICON;
    }
    
}
