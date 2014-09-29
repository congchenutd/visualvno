package com.fujitsu.us.visualvno.parts;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.Request;

import com.fujitsu.us.visualvno.figures.PortFigure;
import com.fujitsu.us.visualvno.figures.ShapeFigure;

public class PortPart extends ShapePart
{
    @Override
    protected IFigure createFigure() {
        return new PortFigure(0);
    }
    
    @Override
    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
        return getConnectionAnchor();
    }

    @Override
    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        return getConnectionAnchor();
    }

    @Override
    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
        return getConnectionAnchor();
    }

    @Override
    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        return getConnectionAnchor();
    }
    
    /**
     * @return  the only port anchor
     */
    protected ConnectionAnchor getConnectionAnchor() {
        return ((ShapeFigure) getFigure()).getPortAnchor(1);
    }
}
