package org.eclipse.gef.examples.shapes.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.examples.shapes.figures.PortFigure;

public class PortEditPart extends ShapeEditPart
{

    @Override
    protected IFigure createFigure() {
        return new PortFigure();
    }

}
