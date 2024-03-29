package com.fujitsu.us.visualvno.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

import com.fujitsu.us.visualvno.model.DiagramModel;

/**
 * Edit part for the diagram model
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class DiagramEditPart extends ContainerEditPart
{

    @Override
    protected void createEditPolicies()
    {
        super.createEditPolicies();
        
        // make the diagram not removable
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
        
        // no links for diagram
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, null);
    }

    @Override
    protected IFigure createFigure()
    {
        Figure figure = new FreeformLayer();
        figure.setBorder(new MarginBorder(3));
        figure.setLayoutManager(new FreeformLayout());

        // Create the static router for the connection layer
        ConnectionLayer connLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
        connLayer.setAntialias(SWT.ON);
        connLayer.setConnectionRouter(new ShortestPathConnectionRouter(figure));

        return figure;
    }
    
    /**
     * Listens to property changes of the model
     */
    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        String prop = event.getPropertyName();
        if(DiagramModel.CHILD_ADDED_PROP  .equals(prop) ||
           DiagramModel.CHILD_REMOVED_PROP.equals(prop))
        {
            refreshChildren();
            refreshFont();
        }
        else if(DiagramModel.FONTSIZE_PROP.equals(prop))
            refreshFont();
        else
            super.propertyChange(event);
    }
    
    @SuppressWarnings("unchecked")
    protected void refreshFont()
    {
        Font font = ((DiagramModel) getModel()).getFont();
        List<AbstractGraphicalEditPart> children = getChildren();
        for(AbstractGraphicalEditPart part: children)
        {
            // shape
            part.getFigure().setFont(font);
            
            // its connections
            List<AbstractConnectionEditPart> sourceLinks = part.getSourceConnections();
            for(AbstractConnectionEditPart link: sourceLinks)
                link.getFigure().setFont(font);
            List<AbstractConnectionEditPart> targetLinks = part.getTargetConnections();
            for(AbstractConnectionEditPart link: targetLinks)
                link.getFigure().setFont(font);
        } 
    }
    
    @Override
    protected void refreshVisuals() {}
    
    @Override
    public IFigure getContentPane() {
        return getFigure();  // the figure itself
    }
    
    @Override
    protected ConnectionAnchor getAnchor() {
        return null;  // not allowed to connect
    }

}