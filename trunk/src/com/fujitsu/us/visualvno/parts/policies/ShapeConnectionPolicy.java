package com.fujitsu.us.visualvno.parts.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.fujitsu.us.visualvno.figures.PortAnchor;
import com.fujitsu.us.visualvno.model.LinkModel;
import com.fujitsu.us.visualvno.model.PortModel;
import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.model.commands.LinkCreateCommand;
import com.fujitsu.us.visualvno.model.commands.LinkReconnectCommand;
import com.fujitsu.us.visualvno.parts.ShapePart;

/**
 * EditPolicy for creating and reconnecting connections
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class ShapeConnectionPolicy extends GraphicalNodeEditPolicy
{
    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request)
    {
        PortModel port = getPort((PortAnchor) getEditPart().getTargetConnectionAnchor(request));
        if(port.getLink() != null)
            return null;
        
        LinkCreateCommand command = (LinkCreateCommand) request.getStartCommand();
        command.setTarget(port);
        return command;
    }

    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request)
    {
        int style = ((Integer) request.getNewObjectType()).intValue();
        if(style == LinkModel.DASHED_CONNECTION)
        {

        }
        
        PortModel port = getPort((PortAnchor) getEditPart().getSourceConnectionAnchor(request));
        if(port.getLink() != null)
            return null;
        
        LinkCreateCommand command = new LinkCreateCommand(port, style);
        request.setStartCommand(command);
        return command;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request)
    {
        PortModel port = getPort((PortAnchor) getEditPart().getSourceConnectionAnchor(request));
        if(port.getLink() != null)
            return null;
        
        LinkModel linkModel = (LinkModel) request.getConnectionEditPart().getModel();
        LinkReconnectCommand command = new LinkReconnectCommand(linkModel);
        command.setNewSource(port);
        return command;
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request)
    {
        PortModel port = getPort((PortAnchor) getEditPart().getTargetConnectionAnchor(request));
        if(port.getLink() != null)
            return null;
        
        LinkModel linkModel = (LinkModel) request.getConnectionEditPart().getModel();
        LinkReconnectCommand command = new LinkReconnectCommand(linkModel);
        command.setNewTarget(port);
        return command;
    }
    
    private ShapePart getEditPart() {
        return (ShapePart) getHost();
    }
    
    private PortModel getPort(PortAnchor anchor) {
        return ((ShapeModel) getHost().getModel()).getPort(anchor.getPortNumber());
    }
}
