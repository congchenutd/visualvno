package com.fujitsu.us.visualvno.model;

import org.eclipse.swt.graphics.Image;

/**
 * A rectangular shape.
 */
public class RectangularShape extends Shape
{
    /** A 16x16 pictogram of a rectangular shape. */
    private static final Image RECTANGLE_ICON   = createImage("icons/rectangle16.gif");

    private static final long  serialVersionUID = 1;

    @Override
    public Image getIcon() {
        return RECTANGLE_ICON;
    }

    @Override
    public String toString() {
        return "Host " + hashCode();
    }
}
