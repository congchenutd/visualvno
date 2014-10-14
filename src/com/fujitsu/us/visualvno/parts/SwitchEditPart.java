package com.fujitsu.us.visualvno.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;

import com.fujitsu.us.visualvno.figures.SwitchFigure;
import com.fujitsu.us.visualvno.model.SwitchModel;

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
