package com.fujitsu.us.visualvno.parts.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.fujitsu.us.visualvno.model.LinkModel;
import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.model.commands.LinkCreateCommand;
import com.fujitsu.us.visualvno.model.commands.LinkReconnectCommand;

/**
 * EditPolicy for creating and reconnecting connections
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class ShapeConnectionEditPolicy extends GraphicalNodeEditPolicy
{
    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request)
    {
        LinkCreateCommand command 
            = (LinkCreateCommand) request.getStartCommand();
        command.setTarget((ShapeModel) getHost().getModel());
        return command;
    }

    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request)
    {
        ShapeModel source = (ShapeModel) getHost().getModel();
        int style = ((Integer) request.getNewObjectType()).intValue();
        LinkCreateCommand command = new LinkCreateCommand(source, 
                                                                      style);
        request.setStartCommand(command);
        return command;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request)
    {
        LinkModel connection 
            = (LinkModel) request.getConnectionEditPart().getModel();
        ShapeModel newSource = (ShapeModel) getHost().getModel();
        LinkReconnectCommand command 
            = new LinkReconnectCommand(connection);
        command.setNewSource(newSource);
        return command;
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request)
    {
        LinkModel connection 
            = (LinkModel) request.getConnectionEditPart().getModel();
        ShapeModel newTarget = (ShapeModel) getHost().getModel();
        LinkReconnectCommand command 
            = new LinkReconnectCommand(connection);
        command.setNewTarget(newTarget);
        return command;
    }
}
