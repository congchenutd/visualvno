package org.eclipse.gef.examples.shapes.parts.policies;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.examples.shapes.model.ContainerModel;
import org.eclipse.gef.examples.shapes.model.DiagramModel;
import org.eclipse.gef.examples.shapes.model.PortModel;
import org.eclipse.gef.examples.shapes.model.ShapeBase;
import org.eclipse.gef.examples.shapes.model.commands.AddCommand;
import org.eclipse.gef.examples.shapes.model.commands.SetConstraintCommand;
import org.eclipse.gef.examples.shapes.model.commands.ShapeCreateCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;


/**
 * EditPolicy for creating, moving, and resizing shapes in a container
 */
public class ContainerLayoutPolicy extends XYLayoutEditPolicy
{

    /**
     * Add a child to a container (this)
     */
    @Override
    protected Command createAddCommand(
                        ChangeBoundsRequest request, EditPart child, Object constraint)
    {
        ContainerModel parent = (ContainerModel) getHost().getModel();
        ShapeBase      shape  = (ShapeBase)      child.getModel();
        
        // disallow dragging a port out of a switch and onto the diagram
        if(parent instanceof DiagramModel &&
           shape  instanceof PortModel)
            return null;
        
        Rectangle  bound = (Rectangle) constraint;
        AddCommand add   = new AddCommand(parent, shape, -1);
        return add.chain(new SetConstraintCommand(shape, bound));
    }

    @Override
    protected Command createChangeConstraintCommand(
                        ChangeBoundsRequest request, EditPart child, Object constraint)
    {
        ShapeBase shape = (ShapeBase) child.getModel();
        Rectangle bound = (Rectangle) constraint;
        return new SetConstraintCommand(shape, bound);
    }

    @Override
    protected Command getCreateCommand(CreateRequest request)
    {
        ShapeBase      shape  = (ShapeBase)      request.getNewObject();
        ContainerModel parent = (ContainerModel) getHost().getModel();
        Rectangle      bound  = (Rectangle)      getConstraintFor(request);
        return new ShapeCreateCommand(shape, parent, bound);
    }

}