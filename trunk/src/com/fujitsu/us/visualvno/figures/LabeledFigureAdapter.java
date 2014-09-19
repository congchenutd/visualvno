package com.fujitsu.us.visualvno.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * An adapter to add a label to a figure
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class LabeledFigureAdapter extends Figure
{
    private final Label  _label;
    private final Figure _figure;

    public LabeledFigureAdapter(Figure figure)
    {
        setLayoutManager(new XYLayout());
        _figure = figure;
        _label = new Label("Shape");
        add(_figure);
        add(_label);
    }

    @Override
    protected void paintFigure(Graphics graphics)
    {
        Rectangle rect = getBounds().getCopy();
        setConstraint(_figure, new Rectangle(0, 0, rect.width, rect.height));
        setConstraint(_label,  new Rectangle(0, 0, rect.width, rect.height));
    }

    public Label getLabel() {
        return _label;
    }
    
    public void setText(String text) {
        _label.setText(text);
    }
}
