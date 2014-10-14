package com.fujitsu.us.visualvno.parts;

import org.eclipse.draw2d.IFigure;

import com.fujitsu.us.visualvno.figures.PortFigure;

public class PortEditPart extends ShapeEditPart
{

    @Override
    protected IFigure createFigure() {
        return new PortFigure();
    }

}
