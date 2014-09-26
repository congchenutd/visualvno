package com.fujitsu.us.visualvno.figures;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

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
{    private final int _number;
    
    public PortAnchor(IFigure owner, int number)
    {
        super(owner);
        _number = number;
    }

    public int getPortNumber() {
        return _number;
    }

    @Override
    public Point getLocation(Point reference) {
        return ((ShapeFigure) getOwner()).getAnchorLocation(getPortNumber());
    }
    
    @Override
    public Point getReferencePoint() {
        return getLocation(null);
    }
}
