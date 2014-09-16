package com.fujitsu.us.visualvno.model;

import org.eclipse.swt.graphics.Image;

/**
 * An elliptical shape.
 */
public class EllipticalShape extends Shape
{
	/** A 16x16 pictogram of an elliptical shape. */
	private static final Image ELLIPSE_ICON = createImage("icons/ellipse16.gif");

	private static final long serialVersionUID = 1;

	@Override
    public Image getIcon() {
		return ELLIPSE_ICON;
	}

}
