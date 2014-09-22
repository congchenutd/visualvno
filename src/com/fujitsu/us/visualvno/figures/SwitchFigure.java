package com.fujitsu.us.visualvno.figures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

public class SwitchFigure extends Shape
{
    private final Map<Integer, ConnectionAnchor> _anchors = new HashMap<Integer, ConnectionAnchor>();

    private int _portCount = 3;
    
    private final Label   _label    = new Label("Switch");
    private final Ellipse _ellipse  = new Ellipse();
    private final List<LabeledShapeAdapter> _ports = new ArrayList<LabeledShapeAdapter>();

    public SwitchFigure()
    {
        setLayoutManager(new XYLayout());
        _ellipse.setOpaque(false);
        _ellipse.setAntialias(SWT.ON);
        add(_ellipse);
        add(_label);
        setPortCount(3);
    }
    
    @Override
    protected void fillShape(Graphics graphics)
    {
        Rectangle rect = getBounds().getCopy();
        setConstraint(_ellipse, new Rectangle(0, 0, rect.width, rect.height));
        setConstraint(_label,   new Rectangle(0, 0, rect.width, rect.height));
        
        int radius = rect.width() / 2;
        for(int i = 0; i < _portCount; ++i)
        {
            double theta = 2 * Math.PI / _portCount * i;
            int x = (int) (radius + radius * Math.sin(theta));
            int y = (int) (radius - radius * Math.cos(theta));
            setConstraint(_ports.get(i), new Rectangle(x, y, 20, 20));
        }
    }
    
    @Override
    public void paintFigure(Graphics graphics)
    {
        super.paintFigure(graphics);
        
//        Rectangle rect = getBounds().getCopy();
//        graphics.setAntialias(SWT.ON);
////        graphics.setBackgroundColor(getBackgroundColor());
////        graphics.drawOval(rect);
////        graphics.fillOval(rect);
//        
//        if(_portCount < 1)
//            return;
//        
//        int radius = rect.width() / 2;
//        int centerX = rect.x() + radius;
//        int centerY = rect.y() + radius;
//        for(int i = 0; i < _portCount; ++i)
//        {
//            double theta = 2 * Math.PI / _portCount * i;
//            int x = (int) (centerX + radius * Math.sin(theta));
//            int y = (int) (centerY - radius * Math.cos(theta));
//            graphics.setBackgroundColor(ColorConstants.white);
//            graphics.drawRectangle(x, y, 20, 20);
//            graphics.drawText(String.valueOf(i+1), x, y);
//        }
    }
    
    public Label getLabel() {
        return _label;
    }
    
    public void setText(String text) {
        _label.setText(text);
    }
    
    public void setPortCount(int count)
    {
        _portCount = count;
        _ports.clear();
        for(int i = 0; i < count; ++i)
        {
            LabeledShapeAdapter port = new LabeledShapeAdapter(new RectangleFigure());
            port.setBackgroundColor(ColorConstants.white);
            port.setText(String.valueOf(i + 1));
            _ports.add(port);
            add(port);
        }
    }

    @Override
    protected void outlineShape(Graphics graphics) {}
}
