package com.fujitsu.us.visualvno.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.fujitsu.us.visualvno.model.Connection;
import com.fujitsu.us.visualvno.model.ModelBase;
import com.fujitsu.us.visualvno.model.commands.ConnectionDeleteCommand;

/**
 * Edit part for Connection model elements.
 */
class ConnectionEditPart extends AbstractConnectionEditPart
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

    private Connection getCastedModel() {
        return (Connection) getModel();
    }

    @Override
    protected void createEditPolicies()
    {
        // Makes the connection show a feedback, when selected by the user.
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, 
                          new ConnectionEndpointEditPolicy());

        // Allows the removal of the connection model element
        installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy()
        {
            @Override
            protected Command getDeleteCommand(GroupRequest request) {
                return new ConnectionDeleteCommand(getCastedModel());
            }
        });
    }

    @Override
    protected IFigure createFigure()
    {
        PolylineConnection connection = (PolylineConnection) super.createFigure();
//        connection.setTargetDecoration(new PolygonDecoration());
        connection.setLineStyle(getCastedModel().getLineStyle());
        return connection;
    }

    /**
     * Reacts to property change
     */
    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        String property = event.getPropertyName();
        if(Connection.LINESTYLE_PROP.equals(property))
            ((PolylineConnection) getFigure()).setLineStyle(getCastedModel().getLineStyle());
    }

}
