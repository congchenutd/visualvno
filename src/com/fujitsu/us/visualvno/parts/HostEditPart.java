package com.fujitsu.us.visualvno.parts;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.Request;

import com.fujitsu.us.visualvno.figures.PortAnchor;

public class HostEditPart extends ShapeEditPart
{
    private ConnectionAnchor _anchor;

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
    
    protected ConnectionAnchor getConnectionAnchor()
    {
        if(_anchor == null)
            _anchor = new PortAnchor(1);
        return _anchor;
    }
}
