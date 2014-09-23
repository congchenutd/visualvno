package com.fujitsu.us.visualvno.figures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

public class SwitchFigure extends Ellipse
{
    private int _portCount = 0;
    private final Map<Integer, PortAnchor>  _anchors = new HashMap<Integer, PortAnchor>();
    private final List<LabeledShapeAdapter> _ports   = new ArrayList<LabeledShapeAdapter>();

    public SwitchFigure()
    {
        setLayoutManager(new XYLayout());
        setAntialias(SWT.ON);
        setPortCount(6);
    }
    
    public int getPortCount() {
        return _portCount;
    }
    
    @Override
    protected void fillShape(Graphics graphics)
    {
        super.fillShape(graphics);
        Rectangle rect = getBounds().getCopy();
        int radius = rect.width() / 2 - PortFigure.RADIUS;
        for(int i = 0; i < _portCount; ++i)
        {
            double theta = 2 * Math.PI / _portCount * i;
            int x = (int) (radius + radius * Math.sin(theta));
            int y = (int) (radius - radius * Math.cos(theta));
            setConstraint(_ports.get(i), new Rectangle(x, y, PortFigure.WIDTH, PortFigure.WIDTH));
        }
    }
    
    public void setPortCount(int count)
    {
        // clear existing ports and anchors
        for(int i = 0; i < _portCount; ++i)
            remove(_ports.get(i));
        _portCount = count;
        _ports.clear();
        _anchors.clear();
        
        // add new ports and anchors
        for(int i = 0; i < count; ++i)
        {
            PortFigure port = new PortFigure(i + 1);
            _ports.add(port);
            add(port);
            
            _anchors.put(i + 1, new PortAnchor(i + 1));
        }
    }

    /**
     * Find the anchor closest to the given point
     */
    public ConnectionAnchor getAnchorByLocation(Point point)
    {
        if(_portCount == 0)
            return null;
        
        ConnectionAnchor closest = new EllipseAnchor(this);
        if(point == null)
            return closest;
        
        long min = Long.MAX_VALUE;

        for(PortAnchor anchor: _anchors.values())
        {
            Point p2 = anchor.getLocation(null);
            long d = (long) point.getDistance(p2);
            if(d < min)
            {
                min = d;
                closest = anchor;
            }
        }
        return closest;
    }
    
    public ConnectionAnchor getAnchorByNumber(int number) {
        return _anchors.get(number);
    }
    
}
