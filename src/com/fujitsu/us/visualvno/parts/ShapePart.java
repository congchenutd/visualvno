package com.fujitsu.us.visualvno.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Color;

import com.fujitsu.us.visualvno.figures.LabeledShape;
import com.fujitsu.us.visualvno.figures.ShapeFigure;
import com.fujitsu.us.visualvno.model.LinkModel;
import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.parts.policies.ShapeConnectionPolicy;
import com.fujitsu.us.visualvno.parts.policies.ShapeRemovalPolicy;
import com.fujitsu.us.visualvno.parts.policies.ShapeRenamePolicy;

/**
 * EditPart for all Shape instances
 */
public abstract class ShapePart extends     AbstractGraphicalEditPart 
                                implements  PropertyChangeListener, NodeEditPart
{
    @Override
    public void activate()
    {
        if(!isActive())
        {
            super.activate();
            getCastedModel().addPropertyChangeListener(this);
        }
    }
    
    @Override
    public void deactivate()
    {
        if(isActive())
        {
            super.deactivate();
            getCastedModel().removePropertyChangeListener(this);
        }
    }

    @Override
    protected void createEditPolicies()
    {
        // allow removal of the shape
        installEditPolicy(EditPolicy.COMPONENT_ROLE, 
                          new ShapeRemovalPolicy());

        // allow creation and reconnection of connections between shapes
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, 
                          new ShapeConnectionPolicy());
        
        // double click to edit the label
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
                          new ShapeRenamePolicy());
    }
    
    @Override
    public void performRequest(Request req) {
        if(req.getType() == RequestConstants.REQ_OPEN ||
           req.getType() == RequestConstants.REQ_DIRECT_EDIT)
            performDirectEditing();
    }

    private void performDirectEditing()
    {
        Label label = ((LabeledShape) getFigure()).getLabel();
        LabelDirectEditManager manager 
            = new LabelDirectEditManager(this, 
                                         TextCellEditor.class, 
                                         new LabelCellEditorLocator(label), 
                                         label);
        manager.show();
    }

    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        String property = event.getPropertyName();
        if(ShapeModel.SOURCELINK_PROP.equals(property))
            refreshSourceConnections();
        else if(ShapeModel.TARGETLINK_PROP.equals(property))
            refreshTargetConnections();
        else
            refreshVisuals();
    }

    @Override
    protected void refreshVisuals()
    {
        IFigure f = getFigure();
        ShapeFigure figure = (ShapeFigure) getFigure();
        
        // notify parent container of changed position & location
        Rectangle bounds = new Rectangle(getCastedModel().getLocation(), 
                                         getCastedModel().getSize());
        ((GraphicalEditPart) getParent()).setLayoutConstraint(this, 
                                                              figure, 
                                                              bounds);
        
        figure.setBackgroundColor(new Color(null, getCastedModel().getColor()));

        // update label
        figure.setText(getCastedModel().getName());
        
        figure.setPortCount(getCastedModel().getPortCount());
    }
    
    @Override
    protected List<LinkModel> getModelSourceConnections() {
        return getCastedModel().getSourceLinks();
    }

    @Override
    protected List<LinkModel> getModelTargetConnections() {
        return getCastedModel().getTargetLinks();
    }
    
    private ShapeModel getCastedModel() {
        return (ShapeModel) getModel();
    }
    
    protected LabeledShape getCastedFigure() {
        return (LabeledShape) getFigure();
    }
}
