package org.eclipse.gef.examples.shapes.parts.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.examples.shapes.model.ILinkEnd;
import org.eclipse.gef.examples.shapes.model.LinkModel;
import org.eclipse.gef.examples.shapes.model.commands.LinkCreateCommand;
import org.eclipse.gef.examples.shapes.model.commands.LinkReconnectCommand;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

/**
 * EditPolicy for creating and reconnecting connections
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class ShapeConnectionPolicy extends GraphicalNodeEditPolicy
{
    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request)
    {
        LinkCreateCommand cmd = (LinkCreateCommand) request.getStartCommand();
        ILinkEnd target = (ILinkEnd) getHost().getModel();
        cmd.setTarget(target);
        return cmd;
    }

    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request)
    {
        ILinkEnd source = (ILinkEnd) getHost().getModel();
        LinkCreateCommand cmd = new LinkCreateCommand(request.getNewObject(), source);
        request.setStartCommand(cmd);
        return cmd;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request)
    {
        ILinkEnd newSource = (ILinkEnd) getHost().getModel();
        LinkModel link = (LinkModel) request.getConnectionEditPart().getModel();
        LinkReconnectCommand cmd = new LinkReconnectCommand(link);
        cmd.setNewSource(newSource);
        return cmd;
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request)
    {
        ILinkEnd newTarget = (ILinkEnd) getHost().getModel();
        LinkModel link = (LinkModel) request.getConnectionEditPart().getModel();
        LinkReconnectCommand cmd = new LinkReconnectCommand(link);
        cmd.setNewTarget(newTarget);
        return cmd;
    }
}
