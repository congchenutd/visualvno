package com.fujitsu.us.visualvno.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jface.viewers.TextCellEditor;

import com.fujitsu.us.visualvno.figures.ConnectionWithLabel;
import com.fujitsu.us.visualvno.model.ConnectionModel;
import com.fujitsu.us.visualvno.model.ModelBase;
import com.fujitsu.us.visualvno.model.commands.ConnectionDeleteCommand;
import com.fujitsu.us.visualvno.parts.policies.ConnectionRenameEditPolicy;

/**
 * Edit part for Connection model elements.
 */
public class ConnectionEditPart extends AbstractConnectionEditPart
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

    private ConnectionModel getCastedModel() {
        return (ConnectionModel) getModel();
    }

    @Override
    protected void createEditPolicies()
    {
        // show a feedback when selected by the user.
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, 
                          new ConnectionEndpointEditPolicy());

        // Allows the removal of the connection
        installEditPolicy(EditPolicy.CONNECTION_ROLE, 
                          new ConnectionEditPolicy()
        {
            @Override
            protected Command getDeleteCommand(GroupRequest request) {
                return new ConnectionDeleteCommand(getCastedModel());
            }
        });
        
        // double click to edit the label
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
                          new ConnectionRenameEditPolicy());
    }

    @Override
    protected IFigure createFigure()
    {
        ConnectionWithLabel connection = new ConnectionWithLabel();
//        connection.setLineStyle(getCastedModel().getLineStyle());
        connection.setConnectionRouter(new BendpointConnectionRouter());
        return connection;
    }

    @Override
    public void performRequest(Request req) {
        if(req.getType() == RequestConstants.REQ_OPEN)
            performDirectEditing();
    }

    // TODO: how to call this automatically after a new connection is created?
    private void performDirectEditing()
    {
        Label label = ((ConnectionWithLabel) getFigure()).getLabel();
        LabelDirectEditManager manager 
            = new LabelDirectEditManager(this, 
                                         TextCellEditor.class, 
                                         new LabelCellEditorLocator(label), 
                                         label);
        manager.show();
    }
    
    /**
     * Reacts to property change
     */
    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        String property = event.getPropertyName();
        if(ConnectionModel.LINESTYLE_PROP.equals(property) ||
           ConnectionModel.NAME_PROP     .equals(property))
            refreshVisuals();
    }
    
    @Override
    protected void refreshVisuals()
    {
        ConnectionModel model = getCastedModel();
        ConnectionWithLabel connection = (ConnectionWithLabel) getFigure();
        connection.setLineStyle(model.getLineStyle());
        connection.setText     (model.getName());
    }

}
