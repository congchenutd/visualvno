package com.fujitsu.us.visualvno.parts.policies;

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;

public class LinkEndpointEditPolicy extends ConnectionEndpointEditPolicy
{
    /**
     * "Highlight" selected link
     * UGLY: should support flexible line width in the future
     */
    @Override
    protected void addSelectionHandles()
    {
        super.addSelectionHandles();
        getConnectionFigure().setLineWidth(2);
    }

    /**
     * "De-highlight" selected link
     */
    @Override
    protected void removeSelectionHandles()
    {
        super.removeSelectionHandles();
        getConnectionFigure().setLineWidth(0);
    }
    
    private PolylineConnection getConnectionFigure() {
        return (PolylineConnection) 
                ((GraphicalEditPart) getHost()).getFigure();
    }
}