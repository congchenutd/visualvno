package com.fujitsu.us.visualvno.parts.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.fujitsu.us.visualvno.figures.PortAnchor;
import com.fujitsu.us.visualvno.model.LinkModel;
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
        LinkCreateCommand command = (LinkCreateCommand) request.getStartCommand();
        PortAnchor anchor = (PortAnchor) getEditPart().getTargetConnectionAnchor(request);
        ShapeModel shape = (ShapeModel) getHost().getModel();
        command.setTarget(shape.getPort(anchor.getPortNumber()));
        return command;
    }

    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request)
    {
        PortAnchor anchor = (PortAnchor) getEditPart().getSourceConnectionAnchor(request);
        ShapeModel shape = (ShapeModel) getHost().getModel();
        int style = ((Integer) request.getNewObjectType()).intValue();
        LinkCreateCommand command = new LinkCreateCommand(shape.getPort(anchor.getPortNumber()), 
                                                          style);
        request.setStartCommand(command);
        return command;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request)
    {
        LinkModel connection 
            = (LinkModel) request.getConnectionEditPart().getModel();
        PortAnchor anchor = (PortAnchor) getEditPart().getSourceConnectionAnchor(request);
        ShapeModel shape = (ShapeModel) getHost().getModel();
        LinkReconnectCommand command = new LinkReconnectCommand(connection);
        command.setNewSource(shape.getPort(anchor.getPortNumber()));
        return command;
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request)
    {
        LinkModel connection 
            = (LinkModel) request.getConnectionEditPart().getModel();
        PortAnchor anchor = (PortAnchor) getEditPart().getTargetConnectionAnchor(request);
        ShapeModel shape = (ShapeModel) getHost().getModel();
        LinkReconnectCommand command = new LinkReconnectCommand(connection);
        command.setNewTarget(shape.getPort(anchor.getPortNumber()));
        return command;
    }
    
    private ShapeEditPart getEditPart() {
        return (ShapeEditPart) getHost();
    }
}
