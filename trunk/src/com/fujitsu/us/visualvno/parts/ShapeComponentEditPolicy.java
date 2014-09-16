package com.fujitsu.us.visualvno.parts;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.fujitsu.us.visualvno.model.Shape;
import com.fujitsu.us.visualvno.model.ShapesDiagram;
import com.fujitsu.us.visualvno.model.commands.ShapeDeleteCommand;

/**
 * This edit policy enables the removal of a Shape from its container.
 */
class ShapeComponentEditPolicy extends ComponentEditPolicy
{
    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest)
    {
        Object parent = getHost().getParent().getModel();
        Object child  = getHost().getModel();
        if(parent instanceof ShapesDiagram &&
           child  instanceof Shape)
            return new ShapeDeleteCommand((ShapesDiagram) parent, (Shape) child);
        return super.createDeleteCommand(deleteRequest);
    }
}
