package com.fujitsu.us.visualvno.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jface.viewers.TextCellEditor;

import com.fujitsu.us.visualvno.figures.LinkConnection;
import com.fujitsu.us.visualvno.model.LinkBase;
import com.fujitsu.us.visualvno.model.ModelBase;
import com.fujitsu.us.visualvno.model.commands.LinkDeleteCommand;
import com.fujitsu.us.visualvno.parts.policies.LinkEndpointPolicy;
import com.fujitsu.us.visualvno.parts.policies.LinkRenamePolicy;

public class LinkEditPart  extends     AbstractConnectionEditPart 
                    implements  PropertyChangeListener
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
    
    @Override
    protected void createEditPolicies()
    {
        // show a feedback when selected by the user.
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, 
                          new LinkEndpointPolicy());
        
        // allows removal of a connection
        installEditPolicy(EditPolicy.CONNECTION_ROLE,
                new ConnectionEditPolicy()
                {
                    @Override
                    protected Command getDeleteCommand(GroupRequest request) {
                        return new LinkDeleteCommand(getCastedModel());
                    }
                });
        
        // double click to edit the label
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
                          new LinkRenamePolicy());
    }
    
    /**
     * direct edit
     */
    @Override
    public void performRequest(Request req) {
        if(req.getType() == RequestConstants.REQ_OPEN ||
                req.getType() == RequestConstants.REQ_DIRECT_EDIT)
        {
            Label label = ((LinkConnection) getFigure()).getLabel();
            LabelDirectEditManager manager 
            = new LabelDirectEditManager(this, 
                    TextCellEditor.class, 
                    new LabelCellEditorLocator(label), 
                    label);
            manager.show();
        }
    }

    @Override
    protected IFigure createFigure()
    {
        LinkConnection connection = new LinkConnection();
        connection.setLineStyle(getCastedModel().getLineStyle());
        connection.setLineWidth(getCastedModel().getLineWidth());
        return connection;
    }

    private LinkBase getCastedModel() {
        return (LinkBase) getModel();
    }
    
    /**
     * Reacts to property change
     */
    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        String property = event.getPropertyName();
        if(LinkBase.NAME_PROP .equals(property) ||
           LinkBase.STYLE_PROP.equals(property))
            refreshVisuals();
    }
    
    @Override
    protected void refreshVisuals()
    {
        LinkBase model = getCastedModel();
        LinkConnection connection = (LinkConnection) getFigure();
        connection.setLineStyle(model.getLineStyle());
        connection.setText     (model.getName());
    }

}