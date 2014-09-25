package com.fujitsu.us.visualvno.figures;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Anchor associated with a PortFigure
 * 
 * FIXME: currently the anchor's owner is the ShapeFigure, 
 * not the PortFigure (child of ShapeFigure)
 * otherwise, the link is not updated with the moving shape.
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class PortAnchor extends AbstractConnectionAnchor
{    private int _portNumber;
    
    public PortAnchor(IFigure owner, int portNumber)
    {
        super(owner);
        setPortNumber(portNumber);
    }

    public void setPortNumber(int number) {
        _portNumber = number;
    }
    
    public int getPortNumber() {
        return _portNumber;
    }

    @Override
    public Point getLocation(Point reference)
    {
        ShapeFigure figure = (ShapeFigure) getOwner();
        Rectangle rect = figure.getBounds();
        int radius = rect.width() / 2 - PortFigure.RADIUS;
        
        double theta = 2 * Math.PI * _portNumber / figure.getPortCount();
        int dx =  (int) (radius * Math.sin(theta));
        int dy = -(int) (radius * Math.cos(theta));
        
        return rect.getCenter().translate(dx, dy);
    }
    
    @Override
    public Point getReferencePoint() {
        return getLocation(null);
    }
}
