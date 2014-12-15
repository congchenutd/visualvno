package com.fujitsu.us.visualvno.figures;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A square figure
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class SquareFigure extends ShapeFigure
{

    @Override
    protected void fillShape(Graphics graphics)
    {
        super.fillShape(graphics);
        graphics.fillRectangle(getBounds());  // fill a rectangle
    }

    /**
     * Draw the outline
     */
    @Override
    protected void outlineShape(Graphics graphics)
    {
        float lineInset = Math.max(1.0f, getLineWidthFloat()) / 2.0f;
        int inset1 = (int) Math.floor(lineInset);
        int inset2 = (int) Math.ceil(lineInset);

        Rectangle r = Rectangle.SINGLETON.setBounds(getBounds());
        r.x += inset1;
        r.y += inset1;
        r.width -= inset1 + inset2;
        r.height -= inset1 + inset2;

        graphics.drawRectangle(r);
    }

	@Override
	public ConnectionAnchor getAnchor()
	{
		if(_anchor == null)
			return new ChopboxAnchor(this);  // lazy creation of the anchor
		return _anchor;
	}
    
}
