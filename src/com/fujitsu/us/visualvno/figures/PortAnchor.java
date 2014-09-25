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
{    private final PortFigure _port;
    
    public PortAnchor(IFigure owner, PortFigure port)
    {
        super(owner);
        _port = port;
    }

    public int getPortNumber() {
        return _port.getNumber();
    }

    @Override
    public Point getLocation(Point reference)
    {
        ShapeFigure figure = (ShapeFigure) getOwner();
        Rectangle rect = figure.getPortBounds(getPortNumber());
        rect.translate(figure.getLocation());  // convert to absolute
        return rect.getCenter();
    }
    
    @Override
    public Point getReferencePoint() {
        return getLocation(null);
    }
}
