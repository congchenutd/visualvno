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
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Color;

import com.fujitsu.us.visualvno.figures.LabeledFigureAdapter;
import com.fujitsu.us.visualvno.model.Connection;
import com.fujitsu.us.visualvno.model.Switch;
import com.fujitsu.us.visualvno.model.ModelBase;
import com.fujitsu.us.visualvno.model.Host;
import com.fujitsu.us.visualvno.model.Shape;
import com.fujitsu.us.visualvno.model.commands.ConnectionCreateCommand;
import com.fujitsu.us.visualvno.model.commands.ConnectionReconnectCommand;
import com.fujitsu.us.visualvno.policies.ShapeRemovalEditPolicy;
import com.fujitsu.us.visualvno.policies.ShapeRenameEditPolicy;

/**
 * EditPart used for Shape instances
 */
public class ShapeEditPart extends AbstractGraphicalEditPart 
                           implements PropertyChangeListener, NodeEditPart
{

    private ConnectionAnchor anchor;

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

    private Shape getCastedModel() {
        return (Shape) getModel();
    }

    @Override
    protected void createEditPolicies()
    {
        // allow removal of the associated model element
        installEditPolicy(EditPolicy.COMPONENT_ROLE, 
                          new ShapeRemovalEditPolicy());

        // allow the creation of connections and
        // the reconnection of connections between Shape instances
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, 
                          new GraphicalNodeEditPolicy()
        {
            @Override
            protected Command getConnectionCompleteCommand(CreateConnectionRequest request)
            {
                ConnectionCreateCommand cmd = (ConnectionCreateCommand) request.getStartCommand();
                cmd.setTarget((Shape) getHost().getModel());
                return cmd;
            }

            @Override
            protected Command getConnectionCreateCommand(CreateConnectionRequest request)
            {
                Shape source = (Shape) getHost().getModel();
                int style = ((Integer) request.getNewObjectType()).intValue();
                ConnectionCreateCommand cmd = new ConnectionCreateCommand(source, style);
                request.setStartCommand(cmd);
                return cmd;
            }

            @Override
            protected Command getReconnectSourceCommand(ReconnectRequest request)
            {
                Connection conn = (Connection) request.getConnectionEditPart().getModel();
                Shape newSource = (Shape) getHost().getModel();
                ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
                cmd.setNewSource(newSource);
                return cmd;
            }

            @Override
            protected Command getReconnectTargetCommand(ReconnectRequest request)
            {
                Connection conn = (Connection) request.getConnectionEditPart().getModel();
                Shape newTarget = (Shape) getHost().getModel();
                ConnectionReconnectCommand cmd = new ConnectionReconnectCommand(conn);
                cmd.setNewTarget(newTarget);
                return cmd;
            }
        });
        
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
    
    @Override
    public void performRequest(Request req) {
        if(req.getType() == RequestConstants.REQ_OPEN)
            performDirectEditing();
    }

    // TODO: how to call this automatically after a new figure is created?
    private void performDirectEditing()
    {
        Label label = ((LabeledFigureAdapter) getFigure()).getLabel();
        ShapeDirectEditManager manager 
            = new ShapeDirectEditManager(this, 
                                         TextCellEditor.class, 
                                         new ShapeCellEditorLocator(label), 
                                         label);
        manager.show();
    }

    /**
     * Return a IFigure depending on the instance of the current model element.
     * This allows this EditPart to be used for both sublasses of Shape.
     */
    private IFigure createFigureForModel()
    {
        if(getModel() instanceof Switch)
            return new LabeledFigureAdapter("Text", new Ellipse());
        else if(getModel() instanceof Host)
            return new LabeledFigureAdapter("Text", new RectangleFigure());
        else
            throw new IllegalArgumentException();
    }

    protected ConnectionAnchor getConnectionAnchor()
    {
        if(anchor == null)
        {
            if(getModel() instanceof Switch)
                anchor = new EllipseAnchor(getFigure());
            else if(getModel() instanceof Host)
                anchor = new ChopboxAnchor(getFigure());
            else
                throw new IllegalArgumentException("unexpected model");
        }
        return anchor;
    }

    @Override
    protected List<Connection> getModelSourceConnections() {
        return getCastedModel().getSourceConnections();
    }

    @Override
    protected List<Connection> getModelTargetConnections() {
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

    @Override
    public void propertyChange(PropertyChangeEvent e)
    {
        String prop = e.getPropertyName();
        if(Shape.SOURCE_PROP.equals(prop))
            refreshSourceConnections();
        else if(Shape.TARGET_PROP.equals(prop))
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
        LabeledFigureAdapter figure = (LabeledFigureAdapter) getFigure();
        figure.setText(getCastedModel().getName());
        figure.setBackgroundColor(new Color(null, getCastedModel().getColor()));
    }
}
