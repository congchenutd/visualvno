package com.fujitsu.us.visualvno.model;

import org.eclipse.swt.graphics.Image;

/**
 * A rectangular shape.
 */
public class RectangularShape extends Shape
{
    public  static final String imageFileSmall = "icons/rectangle16.gif";
    public  static final String imageFileBig   = "icons/rectangle24.gif";
    private static final Image ICON = createImage(imageFileSmall);

    private static final long  serialVersionUID = 1;

    @Override
    public Image getIcon() {
        return ICON;
    }

}
