package com.fujitsu.us.visualvno.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;

public class PortFigure extends LabeledShapeAdapter
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
}
