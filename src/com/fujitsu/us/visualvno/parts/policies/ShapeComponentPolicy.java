package com.fujitsu.us.visualvno.parts.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.model.ShapeBase;
import com.fujitsu.us.visualvno.model.commands.ShapeDeleteCommand;

/**
 * Allows the removal of a child
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class ShapeComponentPolicy extends ComponentEditPolicy
{
    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest)
    {
        Object parent = getHost().getParent().getModel();
        Object child  = getHost().getModel();
        if(parent instanceof DiagramModel && child instanceof ShapeBase)
            return new ShapeDeleteCommand((DiagramModel) parent, (ShapeBase) child);
        return super.createDeleteCommand(deleteRequest);
    }
}