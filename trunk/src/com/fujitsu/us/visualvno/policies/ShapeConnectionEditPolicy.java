package com.fujitsu.us.visualvno.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.fujitsu.us.visualvno.model.ConnectionModel;
import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.model.commands.ConnectionCreateCommand;
import com.fujitsu.us.visualvno.model.commands.ConnectionReconnectCommand;

/**
 * EditPolicy for creating and reconnecting connections
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class ShapeConnectionEditPolicy extends GraphicalNodeEditPolicy
{
    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request)
    {
        ConnectionCreateCommand command 
            = (ConnectionCreateCommand) request.getStartCommand();
        command.setTarget((ShapeModel) getHost().getModel());
        return command;
    }

    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request)
    {
        ShapeModel source = (ShapeModel) getHost().getModel();
        int style = ((Integer) request.getNewObjectType()).intValue();
        ConnectionCreateCommand command = new ConnectionCreateCommand(source, 
                                                                      style);
        request.setStartCommand(command);
        return command;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request)
    {
        ConnectionModel connection 
            = (ConnectionModel) request.getConnectionEditPart().getModel();
        ShapeModel newSource = (ShapeModel) getHost().getModel();
        ConnectionReconnectCommand command 
            = new ConnectionReconnectCommand(connection);
        command.setNewSource(newSource);
        return command;
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request)
    {
        ConnectionModel connection 
            = (ConnectionModel) request.getConnectionEditPart().getModel();
        ShapeModel newTarget = (ShapeModel) getHost().getModel();
        ConnectionReconnectCommand command 
            = new ConnectionReconnectCommand(connection);
        command.setNewTarget(newTarget);
        return command;
    }
}
