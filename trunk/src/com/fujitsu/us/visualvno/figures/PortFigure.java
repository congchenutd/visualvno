package com.fujitsu.us.visualvno.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * A figure for a port
 * Child of a ShapeFigure
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class PortFigure extends LabeledShape
{
    public static final int RADIUS = 10;
    public static final int WIDTH  = 2 * RADIUS;
    
    public PortFigure(int number)
    {
        super(new RectangleFigure());
        setBackgroundColor(ColorConstants.white);
        setNumber(number);
    }

    public int getNumber() {
        return Integer.valueOf(getText());
    }
    
    public void setNumber(int number) {
        setText(String.valueOf(number));
    }
    
    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        return new Dimension(WIDTH, WIDTH);
    }
    
    @Override
    public String toString() {
        return getParent().toString() + " Port " + getNumber();
    }

}
