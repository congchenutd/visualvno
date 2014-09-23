package com.fujitsu.us.visualvno.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;
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

import com.fujitsu.us.visualvno.figures.LabeledShapeAdapter;
import com.fujitsu.us.visualvno.figures.SwitchFigure;
import com.fujitsu.us.visualvno.model.LinkModel;
import com.fujitsu.us.visualvno.model.PortModel;
import com.fujitsu.us.visualvno.model.SwitchModel;
import com.fujitsu.us.visualvno.model.ModelBase;
import com.fujitsu.us.visualvno.model.HostModel;
import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.parts.policies.ShapeConnectionEditPolicy;
import com.fujitsu.us.visualvno.parts.policies.ShapeRemovalEditPolicy;
import com.fujitsu.us.visualvno.parts.policies.ShapeRenameEditPolicy;

/**
 * EditPart for all Shape instances
 */
public class ShapeEditPart extends AbstractGraphicalEditPart 
                           implements PropertyChangeListener, NodeEditPart
{

    private ConnectionAnchor _anchor;

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

    private ShapeModel getCastedModel() {
        return (ShapeModel) getModel();
    }

    @Override
    protected void createEditPolicies()
    {
        // allow removal of the shape
        installEditPolicy(EditPolicy.COMPONENT_ROLE, 
                          new ShapeRemovalEditPolicy());

        // allow creation and reconnection of connections between shapes
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, 
                          new ShapeConnectionEditPolicy());
        
        // double click to edit the label
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
                          new ShapeRenameEditPolicy());
    }

    @Override
    protected IFigure createFigure()
    {
        IFigure figure = createFigureForModel();
        figure.setOpaque(true);
        figure.setBackgroundColor(ColorConstants.green);
        return figure;
    }
    
    /**
     * A Figure factory.
     */
    private IFigure createFigureForModel()
    {
        if(getModel() instanceof HostModel)
            return new LabeledShapeAdapter(new RectangleFigure());
        else if(getModel() instanceof PortModel)
            return new LabeledShapeAdapter(new RectangleFigure());
        else
            throw new IllegalArgumentException();
    }
    
    @Override
    public void performRequest(Request req) {
        if(req.getType() == RequestConstants.REQ_OPEN ||
           req.getType() == RequestConstants.REQ_DIRECT_EDIT)
            performDirectEditing();
    }

    private void performDirectEditing()
    {
        Label label = ((LabeledShapeAdapter) getFigure()).getLabel();
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
        if(ShapeModel.SOURCE_PROP.equals(property))
            refreshSourceConnections();
        else if(ShapeModel.TARGET_PROP.equals(property))
            refreshTargetConnections();
        else
            refreshVisuals();
    }

    @Override
    protected void refreshVisuals()
    {
        // notify parent container of changed position & location
        Rectangle bounds = new Rectangle(getCastedModel().getLocation(), 
                                         getCastedModel().getSize());
        ((GraphicalEditPart) getParent()).setLayoutConstraint(this, 
                                                              getFigure(), 
                                                              bounds);
        
        getFigure().setBackgroundColor(new Color(null, getCastedModel().getColor()));

        // update label
        IFigure figure = getFigure();
        if(figure instanceof LabeledShapeAdapter)
            ((LabeledShapeAdapter) getFigure()).setText(getCastedModel().getName());
    }
    
    @Override
    protected List<LinkModel> getModelSourceConnections() {
        return getCastedModel().getSourceConnections();
    }

    @Override
    protected List<LinkModel> getModelTargetConnections() {
        return getCastedModel().getTargetConnections();
    }

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
        {
            if(getModel() instanceof SwitchModel)
                _anchor = new EllipseAnchor(getFigure());
            else if(getModel() instanceof HostModel)
                _anchor = new ChopboxAnchor(getFigure());
            else
                throw new IllegalArgumentException("unexpected model");
        }
        return _anchor;
    }
}
