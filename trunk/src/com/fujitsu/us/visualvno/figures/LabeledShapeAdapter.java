package com.fujitsu.us.visualvno.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

/**
 * An adapter to add a label to a Shape
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class LabeledShapeAdapter extends Shape
{
    private final Label _label;
    private final Shape _shape;

    public LabeledShapeAdapter(Shape shape)
    {
        setLayoutManager(new XYLayout());
        _shape = shape;
        _shape.setAntialias(SWT.ON);
        _label = new Label("Shape");
        add(_shape);
        add(_label);
    }

    public Label getLabel() {
        return _label;
    }
    
    public String getText() {
        return _label.getText();
    }
    
    public void setText(String text) {
        _label.setText(text);
    }

    @Override
    protected void fillShape(Graphics graphics)
    {
        Rectangle rect = getBounds().getCopy();
        setConstraint(_shape, new Rectangle(0, 0, rect.width, rect.height));
        setConstraint(_label, new Rectangle(0, 0, rect.width, rect.height));
    }

    @Override
    protected void outlineShape(Graphics graphics) {}
}
