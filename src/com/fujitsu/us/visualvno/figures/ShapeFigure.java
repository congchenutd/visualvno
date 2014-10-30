package com.fujitsu.us.visualvno.figures;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

/**
 * Base class for all the figures
 * Provides a label and a content pane (for children)
 * 
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public abstract class ShapeFigure extends Shape
{
    protected Label   _label        = new Label("Label");
    protected IFigure _childrenPane = new FreeformLayer();
    protected ConnectionAnchor _anchor;
    
    public ShapeFigure()
    {
        setAntialias(SWT.ON);
        setLayoutManager(new XYLayout());
        add(_label);
        
        _childrenPane.setLayoutManager(new CircleLayout());
        add(_childrenPane);
    }
    
    @Override
    protected void fillShape(Graphics graphics)
    {
        Rectangle rect = getBounds().getCopy();
        setConstraint(_label,        new Rectangle(0, 0, rect.width, rect.height));
        setConstraint(_childrenPane, new Rectangle(0, 0, rect.width, rect.height));
    }

    public IFigure getContentPane() {
        return _childrenPane;
    }
    
    public Label getLabel() {
        return _label;
    }
    
    public void setText(String text) {
        _label.setText(text);
    }
    
    @Override
    protected boolean useLocalCoordinates() {
        return true;
    }
    
    public abstract ConnectionAnchor getAnchor();
    
}
