package com.fujitsu.us.visualvno.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.model.commands.ShapeDeleteCommand;

/**
 * Edit policy for removal of a Shape from its container.
 */
public class ShapeRemovalEditPolicy extends ComponentEditPolicy
{
    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest)
    {
        Object parent = getHost().getParent().getModel();
        Object child  = getHost().getModel();
        if(parent instanceof DiagramModel &&
           child  instanceof ShapeModel)
            return new ShapeDeleteCommand((DiagramModel) parent, (ShapeModel) child);
        return super.createDeleteCommand(deleteRequest);
    }
}
