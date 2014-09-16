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
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.fujitsu.us.visualvno.model.Switch;
import com.fujitsu.us.visualvno.model.ModelBase;
import com.fujitsu.us.visualvno.model.Host;
import com.fujitsu.us.visualvno.model.Shape;
import com.fujitsu.us.visualvno.model.Diagram;
import com.fujitsu.us.visualvno.model.commands.ShapeCreateCommand;
import com.fujitsu.us.visualvno.model.commands.ShapeSetConstraintCommand;

/**
 * EditPart for a ShapesDiagram.
 */
class DiagramEditPart extends AbstractGraphicalEditPart
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
    
    private Diagram getCastedModel() {
        return (Diagram) getModel();
    }

    @Override
    protected void createEditPolicies()
    {
        // disallows the removal of this edit part from its parent
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
        
        // handles constraint changes (e.g. moving and/or resizing) of model
        // elements and creation of new model elements
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new ShapesXYLayoutEditPolicy());
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
    protected List<Shape> getModelChildren() {
        return getCastedModel().getChildren(); // return a list of shapes
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        String prop = evt.getPropertyName();
        // these properties are fired when Shapes are added into or removed from
        // the ShapeDiagram instance and must cause a call of refreshChildren()
        // to update the diagram's contents.
        if(Diagram.CHILD_ADDED_PROP  .equals(prop) ||
           Diagram.CHILD_REMOVED_PROP.equals(prop))
            refreshChildren();
    }

    /**
     * EditPolicy for the Figure used by this edit part. Children of
     * XYLayoutEditPolicy can be used in Figures with XYLayout.
     */
    private static class ShapesXYLayoutEditPolicy extends XYLayoutEditPolicy
    {

        @Override
        protected Command createChangeConstraintCommand(ChangeBoundsRequest request,
                                                        EditPart child,
                                                        Object constraint)
        {
            // return a command that can move and/or resize a Shape
            if(child      instanceof ShapeEditPart && 
               constraint instanceof Rectangle)
                return new ShapeSetConstraintCommand((Shape) child.getModel(), 
                                                     request,
                                                     (Rectangle) constraint);
            return super.createChangeConstraintCommand(request, child, constraint);
        }

        @Override
        protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
            return null;
        }

        @Override
        protected Command getCreateCommand(CreateRequest request)
        {
            Object childClass = request.getNewObjectType();
            if(childClass == Switch.class || 
               childClass == Host.class)
            {
                // return a command that can add a Shape to a ShapesDiagram
                return new ShapeCreateCommand((Shape) request.getNewObject(),
                                              (Diagram) getHost().getModel(),
                                              (Rectangle) getConstraintFor(request));
            }
            return null;
        }

    }

}
