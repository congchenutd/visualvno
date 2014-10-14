package org.eclipse.gef.examples.shapes.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.examples.shapes.figures.SwitchFigure;
import org.eclipse.gef.examples.shapes.model.SwitchModel;

public class SwitchEditPart extends ContainerEditPart
{
    @Override
    protected IFigure createFigure() {
        return new SwitchFigure();
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        String prop = event.getPropertyName();
        if(SwitchModel.PORTCOUNT_PROP.equals(prop))
            refreshChildren();
        else
            super.propertyChange(event);
    }
    
}
