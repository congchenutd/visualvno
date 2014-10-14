package org.eclipse.gef.examples.shapes.parts.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.examples.shapes.model.DiagramModel;
import org.eclipse.gef.examples.shapes.model.ShapeBase;
import org.eclipse.gef.examples.shapes.model.commands.ShapeDeleteCommand;
import org.eclipse.gef.requests.GroupRequest;

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