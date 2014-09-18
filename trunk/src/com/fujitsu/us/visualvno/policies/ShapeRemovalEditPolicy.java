package com.fujitsu.us.visualvno.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.fujitsu.us.visualvno.model.Shape;
import com.fujitsu.us.visualvno.model.Diagram;
import com.fujitsu.us.visualvno.model.commands.ShapeDeleteCommand;

/**
 * This edit policy enables the removal of a Shape from its container.
 */
public class ShapeRemovalEditPolicy extends ComponentEditPolicy
{
    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest)
    {
        Object parent = getHost().getParent().getModel();
        Object child  = getHost().getModel();
        if(parent instanceof Diagram &&
           child  instanceof Shape)
            return new ShapeDeleteCommand((Diagram) parent, (Shape) child);
        return super.createDeleteCommand(deleteRequest);
    }
}
