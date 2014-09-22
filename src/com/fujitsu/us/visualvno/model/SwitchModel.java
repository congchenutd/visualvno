package com.fujitsu.us.visualvno.model;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * An elliptical shape.
 */
public class SwitchModel extends ShapeModel
{
	private static final long serialVersionUID = 1L;
    public  static final String imageFileSmall  = "icons/ellipse16.gif";
    public  static final String imageFileBig    = "icons/ellipse24.gif";
    private static final Image  ICON            = createImage(imageFileSmall);

	protected static IPropertyDescriptor[] _descriptors;
	
	public static final String DPID_PROP       = "Switch.DPID";
	public static final String PORTCOUNT_PROP  = "Switch.PortCount";
	
	static
	{
	    // copy ShapeModel's descriptors, then add its own
	    _descriptors = new IPropertyDescriptor[ShapeModel.descriptors.length + 2];
	    int i;
	    for(i = 0; i < ShapeModel.descriptors.length; ++i)
	        _descriptors[i] = ShapeModel.descriptors[i];
	    _descriptors[i]    = new TextPropertyDescriptor(DPID_PROP,      "DPID");
	    _descriptors[i+1]  = new TextPropertyDescriptor(PORTCOUNT_PROP, "Port count");
	}
		
	private String _dpid       = new String();
	private int    _portCount  = 0;
		
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
	
	public int getPortCount() {
	    return _portCount;
	}
	
	public void setPortCount(int count)
	{
	    if(count < 0)
            throw new IllegalArgumentException();
	    
	    _portCount = count;
	    firePropertyChange(PORTCOUNT_PROP, null, count);
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
