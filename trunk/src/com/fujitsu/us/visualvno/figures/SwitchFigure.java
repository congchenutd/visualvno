package com.fujitsu.us.visualvno.figures;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A circular shape for switch
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class SwitchFigure extends ShapeFigure
{
    @Override
    protected void fillShape(Graphics graphics)
    {
        super.fillShape(graphics);
        graphics.fillOval(getBounds());  // fill the circle
    }

    @Override
    protected void outlineShape(Graphics graphics) {
        graphics.drawOval(getOptimizedBounds());
    }
    
    /**
     * Generate the outline of the circle
     */
    private Rectangle getOptimizedBounds()
    {
        float lineInset = Math.max(1.0f, getLineWidthFloat()) / 2.0f;
        int inset1 = (int) Math.floor(lineInset);
        int inset2 = (int) Math.ceil(lineInset);

        Rectangle r = Rectangle.SINGLETON.setBounds(getBounds());
        r.x += inset1;
        r.y += inset1;
        r.width -= inset1 + inset2;
        r.height -= inset1 + inset2;
        return r;
    }
    
    @Override
	public ConnectionAnchor getAnchor()
    {
    	if(_anchor == null)
    		return new EllipseAnchor(this);  // lazy creation of the anchor
		return _anchor;
	}

}
