package com.fujitsu.us.visualvno.figures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class ShapeFigure extends LabeledShape
{
    private final List<ConnectionAnchor>    _anchors = new ArrayList<ConnectionAnchor>();
    private final List<PortFigure>          _ports   = new ArrayList<PortFigure>();

    public ShapeFigure(Shape shape)
    {
        super(shape);
        setPortCount(1);
    }
    
    public int getPortCount() {
        return _ports.size();
    }
    
    public void setPortCount(int count)
    {
        // clear existing ports and anchors
        for(int i = 0; i < getPortCount(); ++i)
            remove(_ports.get(i));
        _ports  .clear();
        _anchors.clear();
        
        // add new ports and anchors
        for(int i = 0; i < count; ++i)
        {
            _anchors.add(new PortAnchor(this, i + 1));
            PortFigure port = new PortFigure(i + 1);
            _ports.add(port);
            add(port);
        }
    }

    /**
     * Find the anchor closest to the given point
     */
    public ConnectionAnchor getAnchorByLocation(Point point)
    {
        ConnectionAnchor closest = null;
        long min = Long.MAX_VALUE;
        for(ConnectionAnchor anchor: _anchors)
        {
            long d = (long) point.getDistance(anchor.getLocation(null));
            if(d < min)
            {
                min = d;
                closest = anchor;
            }
        }
        return closest;
    }
    
    public ConnectionAnchor getAnchorByNumber(int number) {
        return _anchors.isEmpty() ? null : _anchors.get(number - 1);
    }

    @Override
    protected void outlineShape(Graphics graphics) {}

    @Override
    protected void fillShape(Graphics graphics)
    {
        super.fillShape(graphics);
        
        for(int i = 0; i < getPortCount(); ++i)
            setConstraint(_ports.get(i), getPortBounds(i));
    }
    
    /**
     * Calculate the bounds of a port
     */
    public Rectangle getPortBounds(int number)
    {
        int radius = getBounds().width() / 2 - PortFigure.RADIUS;
        double theta = 2 * Math.PI / getPortCount() * number;
        int x = (int) (radius + radius * Math.sin(theta));
        int y = (int) (radius - radius * Math.cos(theta));
        return new Rectangle(x, y, PortFigure.WIDTH, PortFigure.WIDTH);
    }
    
    /**
     * Calculate the location of an anchor
     */
    public Point getAnchorLocation(int number)
    {
        Rectangle rect = getPortBounds(number);
        rect.translate(getLocation());  // convert to absolute
        return rect.getCenter();
    }
    
    @Override
    public String toString() {
        return getText();
    }
}
