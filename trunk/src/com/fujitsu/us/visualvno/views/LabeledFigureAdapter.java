package com.fujitsu.us.visualvno.views;

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
    private final Label  label;
    private final Figure figure;

    public LabeledFigureAdapter(String text, Figure figure)
    {
        setLayoutManager(new XYLayout());
        this.figure = figure;
        label = new Label(text);
        add(figure);
        add(label);
    }

    @Override
    protected void paintFigure(Graphics graphics)
    {
        Rectangle rect = getBounds().getCopy();
        setConstraint(figure, new Rectangle(0, 0, rect.width, rect.height));
        setConstraint(label,  new Rectangle(0, 0, rect.width, rect.height));
    }

    public Label getLabel() {
        return label;
    }
    
    public void setText(String text) {
        label.setText(text);
    }
}
