package com.fujitsu.us.visualvno.parts;

import org.eclipse.draw2d.IFigure;

import com.fujitsu.us.visualvno.model.SwitchModel;

public class SwitchEditPart extends ShapeEditPart
{
    protected SwitchModel getCastedModel() {
        return (SwitchModel) getModel();
    }
    
    @Override
    protected IFigure createFigure()
    {
        return null;
//        IFigure figure = createFigureForModel();
//        figure.setOpaque(true);
//        figure.setBackgroundColor(ColorConstants.green);
//        return figure;
    }
}
