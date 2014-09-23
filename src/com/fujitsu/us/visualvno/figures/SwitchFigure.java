package com.fujitsu.us.visualvno.figures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

public class SwitchFigure extends Ellipse
{
    private final Map<Integer, ConnectionAnchor> _anchors = new HashMap<Integer, ConnectionAnchor>();

    private int _portCount = 0;
    
    private final List<LabeledShapeAdapter> _ports = new ArrayList<LabeledShapeAdapter>();

    public SwitchFigure()
    {
        setLayoutManager(new XYLayout());
        setAntialias(SWT.ON);
        setPortCount(6);
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
        for(int i = 0; i < _portCount; ++i)
            remove(_ports.get(i));
        _portCount = count;
        _ports.clear();
        for(int i = 0; i < count; ++i)
        {
            PortFigure port = new PortFigure(i + 1);
            _ports.add(port);
            add(port);
        }
    }

}
