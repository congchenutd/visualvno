package com.fujitsu.us.visualvno.parts.policies;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.swt.graphics.Color;

/**
 * The policy to highlight a container when a child is being dropped into it
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class ContainerHighlightPolicy extends GraphicalEditPolicy
{
    private Color _revertColor;

    @Override
    public void eraseTargetFeedback(Request request)
    {
        if(_revertColor != null)
        {
            setContainerBackground(_revertColor);
            _revertColor = null;
        }
    }

    private Color getContainerBackground() {
        return getContainerFigure().getBackgroundColor();
    }

    private IFigure getContainerFigure() {
        return ((GraphicalEditPart) getHost()).getFigure();
    }

    @Override
    public EditPart getTargetEditPart(Request request) {
        return request.getType().equals(RequestConstants.REQ_SELECTION_HOVER) ? getHost() 
                                                                              : null;
    }

    private void setContainerBackground(Color color) {
        getContainerFigure().setBackgroundColor(color);
    }

    protected void showHighlight()
    {
        if(_revertColor == null)
        {
            _revertColor = getContainerBackground();
            setContainerBackground(ColorConstants.gray);
        }
    }

    @Override
    public void showTargetFeedback(Request request)
    {
        if(request.getType().equals(RequestConstants.REQ_MOVE) || 
           request.getType().equals(RequestConstants.REQ_ADD)  || 
           request.getType().equals(RequestConstants.REQ_CLONE) || 
           request.getType().equals(RequestConstants.REQ_CONNECTION_START) || 
           request.getType().equals(RequestConstants.REQ_CONNECTION_END) || 
           request.getType().equals(RequestConstants.REQ_CREATE))
            showHighlight();
    }
}
