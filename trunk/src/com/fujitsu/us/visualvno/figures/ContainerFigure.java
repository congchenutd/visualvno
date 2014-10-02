package com.fujitsu.us.visualvno.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

public class ContainerFigure extends Figure
{
    @Override
    protected void paintFigure(Graphics graphics)
    {
        Rectangle rect = getBounds().getCopy();
        rect.shrink(new Insets(2, 0, 2, 0));
        graphics.fillRectangle(rect);
    }
}
