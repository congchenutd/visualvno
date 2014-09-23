package com.fujitsu.us.visualvno.figures;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

public class PortAnchor extends AbstractConnectionAnchor
{
    private int _portNumber;
    
    public PortAnchor(int portNumber) {
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
        SwitchFigure figure = (SwitchFigure) getOwner();
        int portCount = figure.getPortCount();
        Rectangle rect = figure.getBounds();
        int radius = rect.width() / 2 - PortFigure.RADIUS;
        
        double theta = 2 * Math.PI / portCount * _portNumber;
        int x = (int) (radius + radius * Math.sin(theta));
        int y = (int) (radius - radius * Math.cos(theta));
        
        Point point = new PrecisionPoint(x, y);
        figure.translateToAbsolute(point);
        return point;
    }
    
    @Override
    public Point getReferencePoint() {
        return getLocation(null);
    }
}
