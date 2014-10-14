package com.fujitsu.us.visualvno.parts.policies;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import com.fujitsu.us.visualvno.model.ContainerModel;
import com.fujitsu.us.visualvno.model.ShapeBase;
import com.fujitsu.us.visualvno.model.commands.OrphanChildCommand;

/**
 * Allow orphan a child from a container
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class ContainerPolicy extends ContainerEditPolicy
{
    @Override
    protected Command getCreateCommand(CreateRequest request) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Command getOrphanChildrenCommand(GroupRequest request)
    {
        CompoundCommand result = new CompoundCommand("Orphan children");
        ContainerModel  parent = (ContainerModel) getHost().getModel();
        List<EditPart>  parts  = request.getEditParts();
        for(EditPart part: parts)
        {
            ShapeBase child = (ShapeBase) part.getModel();
            result.add(new OrphanChildCommand(parent, child));
        }
        return result.unwrap();
    }
}
