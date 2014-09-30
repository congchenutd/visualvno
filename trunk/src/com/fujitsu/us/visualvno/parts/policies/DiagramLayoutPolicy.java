package com.fujitsu.us.visualvno.parts.policies;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.fujitsu.us.visualvno.model.ContainerModel;
import com.fujitsu.us.visualvno.model.HostModel;
import com.fujitsu.us.visualvno.model.PortModel;
import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.model.SwitchModel;
import com.fujitsu.us.visualvno.model.commands.AddToContainerCommand;
import com.fujitsu.us.visualvno.model.commands.ShapeCreateCommand;
import com.fujitsu.us.visualvno.model.commands.ShapeSetConstraintCommand;
import com.fujitsu.us.visualvno.parts.ShapePart;

/**
 * EditPolicy for creating, moving, and resizing shapes in a diagram
 */
public class DiagramLayoutPolicy extends XYLayoutEditPolicy
{

    @Override
    protected Command createChangeConstraintCommand(ChangeBoundsRequest request,
                                                    EditPart child,
                                                    Object constraint)
    {
        // return a command that can move and/or resize a Shape
        if(child      instanceof ShapePart && 
           constraint instanceof Rectangle)
        {
            ShapeModel shape = (ShapeModel) child.getModel();
            Rectangle  rect  = (Rectangle)  constraint;
            return new ShapeSetConstraintCommand(shape, request, rect);
        }
        return super.createChangeConstraintCommand(request, child, constraint);
    }

    @Override
    protected Command createAddCommand(ChangeBoundsRequest request,
                                       EditPart child, Object constraint)
    {
        ContainerModel  container   = (ContainerModel)  getHost().getModel();
        ShapeModel      shape       = (ShapeModel)      child.getModel();
        Rectangle       rect        = (Rectangle)       constraint;
        AddToContainerCommand cmd = new AddToContainerCommand(container, shape, -1);
        return cmd.chain(new ShapeSetConstraintCommand(shape, request, rect));
    }
    
    @Override
    protected Command getCreateCommand(CreateRequest request)
    {
        Object childClass = request.getNewObjectType();
        if(childClass == SwitchModel.class || 
           childClass == HostModel.class   ||
           childClass == PortModel.class)
            return new ShapeCreateCommand((ShapeModel)      request.getNewObject(),
                                          (ContainerModel)  getHost().getModel(),
                                          (Rectangle)       getConstraintFor(request));
        return null;
    }

}