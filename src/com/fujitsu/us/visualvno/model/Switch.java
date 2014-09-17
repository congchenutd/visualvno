package com.fujitsu.us.visualvno.model;

import org.eclipse.swt.graphics.Image;

/**
 * An elliptical shape.
 */
public class Switch extends Shape
{
    public  static final String imageFileSmall = "icons/ellipse16.gif";
    public  static final String imageFileBig   = "icons/ellipse24.gif";
    private static final Image ICON = createImage(imageFileSmall);

	private static final long serialVersionUID = 1;
	
	public static final String DPID_PROP = "Switch.DPID";
	
	private String dpid = null;
	
	public String getDPID() {
		return dpid;
	}
	
	public void setDPID(String dpid) {
		this.dpid = dpid;
		
	}

	@Override
    public Image getIcon() {
		return ICON;
	}
	
	

}