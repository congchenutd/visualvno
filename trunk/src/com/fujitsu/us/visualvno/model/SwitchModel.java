package com.fujitsu.us.visualvno.model;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * An elliptical shape.
 */
public class SwitchModel extends ShapeModel
{
    public  static final String imageFileSmall  = "icons/ellipse16.gif";
    public  static final String imageFileBig    = "icons/ellipse24.gif";
    private static final Image  ICON            = createImage(imageFileSmall);

	protected static IPropertyDescriptor[] _descriptors;
	
	public static final String DPID_PROP = "Switch.DPID";
	
	static
	{
	    // copy ShapeModel's descriptors, then add its own
	    _descriptors = new IPropertyDescriptor[ShapeModel.descriptors.length + 1];
	    int i;
	    for(i = 0; i < ShapeModel.descriptors.length; ++i)
	        _descriptors[i] = ShapeModel.descriptors[i];
	    _descriptors[i] = new TextPropertyDescriptor(DPID_PROP, "DPID");
	}
		
	private String _dpid = new String();
		
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
