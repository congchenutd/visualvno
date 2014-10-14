package com.fujitsu.us.visualvno.parts.policies;

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;

import com.fujitsu.us.visualvno.model.LinkBase;

/**
 * Highlights link selection
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class LinkEndpointPolicy extends ConnectionEndpointEditPolicy
{
    /**
     * "Highlight" selected link
     * TODO: should consider zooming factor
     */
    @Override
    protected void addSelectionHandles()
    {
        super.addSelectionHandles();
        getConnectionFigure().setLineWidth(3 * getOriginalLineWidth());
    }

    /**
     * "De-highlight" selected link
     */
    @Override
    protected void removeSelectionHandles()
    {
        super.removeSelectionHandles();
        getConnectionFigure().setLineWidth(getOriginalLineWidth());
    }

    private int getOriginalLineWidth() {
        return ((LinkBase) getHost().getModel()).getLineWidth();
    }
    
    private PolylineConnection getConnectionFigure() {
        return (PolylineConnection) ((GraphicalEditPart) getHost()).getFigure();
    }
}