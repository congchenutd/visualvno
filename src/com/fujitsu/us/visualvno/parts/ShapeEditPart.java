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
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.swt.graphics.Color;

import com.fujitsu.us.visualvno.model.Connection;
import com.fujitsu.us.visualvno.model.EllipticalShape;
import com.fujitsu.us.visualvno.model.ModelBase;
import com.fujitsu.us.visualvno.model.RectangularShape;
import com.fujitsu.us.visualvno.model.Shape;
import com.fujitsu.us.visualvno.model.commands.ConnectionCreateCommand;
import com.fujitsu.us.visualvno.model.commands.ConnectionReconnectCommand;
import com.fujitsu.us.visualvno.views.LabeledFigureAdapter;

/**
 * EditPart used for Shape instances
 */
class ShapeEditPart extends AbstractGraphicalEditPart 
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
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new ShapeComponentEditPolicy());

        // allow the creation of connections and
        // the reconnection of connections between Shape instances
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new GraphicalNodeEditPolicy()
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
    }

    @Override
    protected IFigure createFigure()
    {
        IFigure f = createFigureForModel();
        f.setOpaque(true);
        f.setBackgroundColor(ColorConstants.green);
        return f;
    }

    /**
     * Return a IFigure depending on the instance of the current model element.
     * This allows this EditPart to be used for both sublasses of Shape.
     */
    private IFigure createFigureForModel()
    {
        if(getModel() instanceof EllipticalShape)
//            return new Ellipse();
            return new LabeledFigureAdapter("Text", new Ellipse());
        else if(getModel() instanceof RectangularShape)
            return new LabeledFigureAdapter("Text", new RectangleFigure());
//            return new RectangleFigure();
        else
            // if Shapes gets extended the conditions above must be updated
            throw new IllegalArgumentException();
    }

    protected ConnectionAnchor getConnectionAnchor()
    {
        if(anchor == null)
        {
            if(getModel() instanceof EllipticalShape)
                anchor = new EllipseAnchor(getFigure());
            else if(getModel() instanceof RectangularShape)
                anchor = new ChopboxAnchor(getFigure());
            else
                // if Shapes gets extended the conditions above must be updated
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
    public void propertyChange(PropertyChangeEvent evt)
    {
        String prop = evt.getPropertyName();
        if(Shape.SIZE_PROP    .equals(prop) ||
           Shape.LOCATION_PROP.equals(prop) ||
           Shape.NAME_PROP    .equals(prop) ||
           Shape.COLOR_PROP   .equals(prop)) {
            refreshVisuals();
        }
        else {
            refreshSourceConnections();
            refreshTargetConnections();
        }
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
