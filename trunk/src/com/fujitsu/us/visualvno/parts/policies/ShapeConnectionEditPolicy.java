package com.fujitsu.us.visualvno.parts.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.fujitsu.us.visualvno.figures.PortAnchor;
import com.fujitsu.us.visualvno.model.LinkModel;
import com.fujitsu.us.visualvno.model.Port;
import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.model.commands.LinkCreateCommand;
import com.fujitsu.us.visualvno.model.commands.LinkReconnectCommand;
import com.fujitsu.us.visualvno.parts.ShapeEditPart;

/**
 * EditPolicy for creating and reconnecting connections
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class ShapeConnectionEditPolicy extends GraphicalNodeEditPolicy
{
    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request)
    {
        Port port = getPort((PortAnchor) getEditPart().getTargetConnectionAnchor(request));
        if(port.getLink() != null)
            return null;
        
        LinkCreateCommand command = (LinkCreateCommand) request.getStartCommand();
        command.setTarget(port);
        return command;
    }

    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request)
    {
        Port port = getPort((PortAnchor) getEditPart().getSourceConnectionAnchor(request));
        if(port.getLink() != null)
            return null;
        
        int style = ((Integer) request.getNewObjectType()).intValue();
        LinkCreateCommand command = new LinkCreateCommand(port, style);
        request.setStartCommand(command);
        return command;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request)
    {
        Port port = getPort((PortAnchor) getEditPart().getSourceConnectionAnchor(request));
        if(port.getLink() != null)
            return null;
        
        LinkModel connection = (LinkModel) request.getConnectionEditPart().getModel();
        LinkReconnectCommand command = new LinkReconnectCommand(connection);
        command.setNewSource(port);
        return command;
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request)
    {
        Port port = getPort((PortAnchor) getEditPart().getTargetConnectionAnchor(request));
        if(port.getLink() != null)
            return null;
        
        LinkModel connection = (LinkModel) request.getConnectionEditPart().getModel();
        LinkReconnectCommand command = new LinkReconnectCommand(connection);
        command.setNewTarget(port);
        return command;
    }
    
    private ShapeEditPart getEditPart() {
        return (ShapeEditPart) getHost();
    }
    
    private Port getPort(PortAnchor anchor) {
        return ((ShapeModel) getHost().getModel()).getPort(anchor.getPortNumber());
    }
}
