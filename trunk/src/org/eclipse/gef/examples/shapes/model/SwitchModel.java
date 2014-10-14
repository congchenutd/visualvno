package org.eclipse.gef.examples.shapes.model;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class SwitchModel extends    ContainerModel
                         implements ILinkEnd
{
    private static final long  serialVersionUID = 1;
    public  static final String SMALL_IMAGE  = "icons/Switch.png";
    public  static final String BIG_IMAGE    = "icons/Switch.png";
    private static final Image  ICON         = createImage(SMALL_IMAGE);
    private static final int    DEFAULT_WIDTH  = 80;
    private static final RGB    DEFAULT_COLOR  = ColorConstants.lightBlue.getRGB();
    
    // property ids
    public static final String DPID_PROP      = "SwitchModel.DPID";
    public static final String PORTCOUNT_PROP = "SwitchModel.PORTCOUNT";

    // descriptors
    protected static IPropertyDescriptor[] _descriptors;
    static
    {
        // copy ShapeBase's descriptors, then add my own
        _descriptors = new IPropertyDescriptor[ShapeBase._descriptors.length + 2];
        int i;
        for(i = 0; i < ShapeBase._descriptors.length; ++i)
            _descriptors[i] = ShapeBase._descriptors[i];
        _descriptors[i]   = new TextPropertyDescriptor(DPID_PROP,      "DPID");
        _descriptors[i+1] = new TextPropertyDescriptor(PORTCOUNT_PROP, "Port count");
        
        ((PropertyDescriptor) _descriptors[i+1]).setValidator(new NumberValidator(0));
    }
    
    // properties
    private String _dpid      = new String();
    private int    _portCount = new Integer(0);
    
    public SwitchModel()
    {
        setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_WIDTH));
        setColor(DEFAULT_COLOR);
    }

    /**
     * Only allow PortModels as children
     */
    @Override
    public boolean canAdd(ShapeBase child) {
        return child instanceof PortModel;
    }
    
    @Override
    public boolean addChild(ShapeBase child, int index)
    {
        boolean result = super.addChild(child, index);
        reorderPortNumbers();
        return result;
    }
    
    @Override
    public boolean removeChild(ShapeBase child)
    {
        boolean result = super.removeChild(child);
        reorderPortNumbers();
        return result;
    }
    
    private void reorderPortNumbers()
    {
        int i = 1;
        for(ShapeBase c: getChildren())
            c.setName(String.valueOf(i++));
    }
    
    public int getPortCount() {
        return _portCount;
    }
    
    public void setPortCount(int count)
    {
        if(count < 0)
            throw new IllegalArgumentException("Port count < 0");
        
        // clear ports and their links
        for(ShapeBase child: getChildren())
            child.clearLinks();
        clearChildren();
        
        // add ports back
        for(int i = 0; i < count; ++i)
        {
            PortModel port = new PortModel(this);
            port.setName(String.valueOf(i + 1));
            addChild(port, i);
        }
        _portCount = count;
        
        firePropertyChange(PORTCOUNT_PROP, null, count);
    }
    
    public String getDPID() {
        return _dpid;
    }
    
    public void setDPID(String dpid)
    {
        if(dpid == null)
            throw new IllegalArgumentException();
        
        _dpid = dpid;
        firePropertyChange(DPID_PROP, null, dpid);
    }
    
    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return _descriptors;
    }
    
    @Override
    public Object getPropertyValue(Object id)
    {
        if(DPID_PROP.equals(id))
            return getDPID();
        if(PORTCOUNT_PROP.equals(id))
            return Integer.toString(getPortCount());
        return super.getPropertyValue(id);
    }
    
    @Override
    public void setPropertyValue(Object id, Object value)
    {
        if(DPID_PROP.equals(id))
            setDPID((String) value);
        else if(PORTCOUNT_PROP.equals(id))
            setPortCount(Integer.parseInt((String) value));
        else
            super.setPropertyValue(id, value);
    }
    
    @Override
    public Image getIcon() {
        return ICON;
    }

    @Override
    public String toString() {
        return "Switch " + getName();
    }
}
