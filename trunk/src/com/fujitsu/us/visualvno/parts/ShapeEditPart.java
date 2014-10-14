package com.fujitsu.us.visualvno.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Color;

import com.fujitsu.us.visualvno.figures.ShapeFigure;
import com.fujitsu.us.visualvno.model.LinkBase;
import com.fujitsu.us.visualvno.model.ModelBase;
import com.fujitsu.us.visualvno.model.ShapeBase;
import com.fujitsu.us.visualvno.parts.policies.ShapeComponentPolicy;
import com.fujitsu.us.visualvno.parts.policies.ShapeConnectionPolicy;
import com.fujitsu.us.visualvno.parts.policies.ShapeRenamePolicy;

public abstract class ShapeEditPart extends    AbstractGraphicalEditPart 
						            implements PropertyChangeListener, NodeEditPart
{

    @Override
    public void activate()
    {
        if(!isActive())
        {
            super.activate();
            
            // add this as a listener of the model
            ((ModelBase) getModel()).addPropertyChangeListener(this);
        }
    }
    
    @Override
    public void deactivate()
    {
        if(isActive())
        {
            super.deactivate();

            // remove this from the listeners of the model
            ((ModelBase) getModel()).removePropertyChangeListener(this);
        }
    }

    @Override
    protected void createEditPolicies()
    {
        // allow removal of the shape
        installEditPolicy(EditPolicy.COMPONENT_ROLE,
                          new ShapeComponentPolicy());

        // allow creation and reconnection of connections between shapes
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, 
                          new ShapeConnectionPolicy());

        // double click to edit the label
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
                          new ShapeRenamePolicy());
    }
    
    /**
     * direct edit
     */
    @Override
    public void performRequest(Request req) {
        if(req.getType() == RequestConstants.REQ_OPEN ||
           req.getType() == RequestConstants.REQ_DIRECT_EDIT)
        {
            Label label = ((ShapeFigure) getFigure()).getLabel();
            LabelDirectEditManager manager 
                = new LabelDirectEditManager(this, 
                                             TextCellEditor.class, 
                                             new LabelCellEditorLocator(label), 
                                             label);
            manager.show();
        }
    }

    protected ShapeBase getCastedModel() {
        return (ShapeBase) getModel();
    }
    
    protected ShapeFigure getCastedFigure() {
        return (ShapeFigure) getFigure();
    }

    @Override
    protected List<LinkBase> getModelSourceConnections() {
        return getCastedModel().getSourceLinks();
    }

    @Override
    protected List<LinkBase> getModelTargetConnections() {
        return getCastedModel().getTargetLinks();
    }

    @Override
    public ConnectionAnchor getSourceConnectionAnchor(
            ConnectionEditPart connection)  {
        return getAnchor();
    }

    @Override
    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        return getAnchor();
    }

    @Override
    public ConnectionAnchor getTargetConnectionAnchor(
            ConnectionEditPart connection) {
        return getAnchor();
    }

    @Override
    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        return getAnchor();
    }

    protected ConnectionAnchor getAnchor() {
    	return ((ShapeFigure) getFigure()).getAnchor();
    }

    /**
     * Listens to property changes
     */
    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        String prop = event.getPropertyName();
        if(ShapeBase.SOURCE_LINKS_PROP.equals(prop))
            refreshSourceConnections();
        else if(ShapeBase.TARGET_LINKS_PROP.equals(prop))
            refreshTargetConnections();
        else
            // location, size, color, name
            refreshVisuals();
    }

    @Override
    protected void refreshVisuals()
    {
        ShapeBase   model  = getCastedModel();
        ShapeFigure figure = getCastedFigure();
        
        // location and size
        Rectangle bounds = new Rectangle(model.getLocation(), model.getSize());
        ((GraphicalEditPart) getParent()).setLayoutConstraint(this, figure, bounds);
        
        // color
        figure.setBackgroundColor(new Color(null, model.getColor()));

        // label
        figure.setText(model.getName());
    }
    
    /**
     * Get the pane that all the children are placed on 
     */
    @Override
    public IFigure getContentPane() {
        return getCastedFigure().getContentPane();
    }
}