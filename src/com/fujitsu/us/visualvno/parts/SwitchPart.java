package com.fujitsu.us.visualvno.parts;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.DropRequest;

import com.fujitsu.us.visualvno.figures.SwitchFigure;
import com.fujitsu.us.visualvno.model.LinkModel;
import com.fujitsu.us.visualvno.model.PortModel;
import com.fujitsu.us.visualvno.model.SwitchModel;

public class SwitchEditPart extends ShapeEditPart
{
    protected SwitchModel getCastedModel() {
        return (SwitchModel) getModel();
    }
    
    @Override
    protected IFigure createFigure() {
        return new SwitchFigure();
    }
    
    private SwitchFigure getSwitchFigure() {
        return (SwitchFigure) getFigure();
    }
    
    @Override
    public ConnectionAnchor getSourceConnectionAnchor(
                                ConnectionEditPart connectionEditPart) {
        LinkModel link = (LinkModel) connectionEditPart.getModel();
        PortModel port = (PortModel) link.getSourcePort();
        return getSwitchFigure().getPortAnchor(port.getNumber());
    }

    @Override
    public ConnectionAnchor getSourceConnectionAnchor(Request request)
    {
        Point pt = new Point(((DropRequest) request).getLocation());
        return getSwitchFigure().getPortAnchor(pt);
    }

    @Override
    public ConnectionAnchor getTargetConnectionAnchor(
                                    ConnectionEditPart connectionEditPart)
    {
        LinkModel link = (LinkModel) connectionEditPart.getModel();
        PortModel port = (PortModel) link.getSourcePort();
        return getSwitchFigure().getPortAnchor(port.getNumber());
    }

    @Override
    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        Point pt = new Point(((DropRequest) request).getLocation());
        return getSwitchFigure().getPortAnchor(pt);
    }
}
