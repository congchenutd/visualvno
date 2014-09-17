package com.fujitsu.us.visualvno.model;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * An elliptical shape.
 */
public class Switch extends Shape
{
    public  static final String imageFileSmall = "icons/ellipse16.gif";
    public  static final String imageFileBig   = "icons/ellipse24.gif";
    private static final Image ICON = createImage(imageFileSmall);

	protected static IPropertyDescriptor[] descriptors;
	
	public static final String DPID_PROP = "Switch.DPID";
	
	static
	{
	    Switch.descriptors = new IPropertyDescriptor[Shape.descriptors.length + 1];
	    int i;
	    for(i = 0; i < Shape.descriptors.length; ++i)
	        Switch.descriptors[i] = Shape.descriptors[i];
	    Switch.descriptors[i] = new TextPropertyDescriptor(DPID_PROP, "DPID");
	}
		
	private String dpid = new String();
		
	public String getDPID() {
		return dpid;
	}
	
	public void setDPID(String dpid)
	{
		this.dpid = dpid;
		firePropertyChange(DPID_PROP, null, dpid);
	}

	@Override
    public Image getIcon() {
		return ICON;
	}
	
	@Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return Switch.descriptors;
    }
	
	@Override
    public Object getPropertyValue(Object id)
    {
        if(DPID_PROP.equals(id))
            return getDPID();
        return super.getPropertyValue(id);
    }
    
    @Override
    public void setPropertyValue(Object id, Object value)
    {
        if(DPID_PROP.equals(id))
            setDPID((String) value);
        else
            super.setPropertyValue(id, value);
    }

}
