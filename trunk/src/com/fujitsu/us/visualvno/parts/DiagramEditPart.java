package com.fujitsu.us.visualvno.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;

import com.fujitsu.us.visualvno.model.ModelBase;
import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.parts.policies.ShapesXYLayoutEditPolicy;

/**
 * EditPart for a ShapesDiagram.
 */
public class DiagramEditPart extends AbstractGraphicalEditPart
                             implements PropertyChangeListener
{

    @Override
    public void activate()
    {
        if(!isActive())
        {
            super.activate();
            ((ModelBase) getModel()).addPropertyChangeListener(this);
        }
    }

    @Override
    public void deactivate()
    {
        if(isActive())
        {
            super.deactivate();
            ((ModelBase) getModel()).removePropertyChangeListener(this);
        }
    }
    
    private DiagramModel getCastedModel() {
        return (DiagramModel) getModel();
    }

    @Override
    protected void createEditPolicies()
    {
        // disallows the removal of this edit part from its parent
        installEditPolicy(EditPolicy.COMPONENT_ROLE, 
                          new RootComponentEditPolicy());
        
        // handles constraint changes (e.g. moving and resizing) of model
        // elements and creation of new model elements
        installEditPolicy(EditPolicy.LAYOUT_ROLE, 
                          new ShapesXYLayoutEditPolicy());
    }

    @Override
    protected IFigure createFigure()
    {
        Figure figure = new FreeformLayer();
        figure.setBorder(new MarginBorder(3));
        figure.setLayoutManager(new FreeformLayout());

        // Create the static router for the connection layer
        ConnectionLayer connLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
        connLayer.setConnectionRouter(new ShortestPathConnectionRouter(figure));

        return figure;
    }

    @Override
    protected List<ShapeModel> getModelChildren() {
        return getCastedModel().getChildren(); // return a list of shapes
    }

    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        String property = event.getPropertyName();
        if(DiagramModel.CHILD_ADDED_PROP  .equals(property) ||
           DiagramModel.CHILD_REMOVED_PROP.equals(property))
            refreshChildren();
    }

}
