package com.fujitsu.us.visualvno.parts.policies;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.model.HostModel;
import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.model.SwitchModel;
import com.fujitsu.us.visualvno.model.commands.ShapeCreateCommand;
import com.fujitsu.us.visualvno.model.commands.ShapeSetConstraintCommand;
import com.fujitsu.us.visualvno.parts.ShapeEditPart;

/**
 * EditPolicy for creating, moving, and resizing shapes in a diagram
 */
public class ShapesXYLayoutEditPolicy extends XYLayoutEditPolicy
{

    @Override
    protected Command createChangeConstraintCommand(ChangeBoundsRequest request,
                                                    EditPart child,
                                                    Object constraint)
    {
        // return a command that can move and/or resize a Shape
        if(child      instanceof ShapeEditPart && 
           constraint instanceof Rectangle)
            return new ShapeSetConstraintCommand((ShapeModel) child.getModel(), 
                                                 request,
                                                 (Rectangle) constraint);
        return super.createChangeConstraintCommand(request, child, constraint);
    }

    @Override
    protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
        return null;
    }

    @Override
    protected Command getCreateCommand(CreateRequest request)
    {
        Object childClass = request.getNewObjectType();
        if(childClass == SwitchModel.class || 
           childClass == HostModel.class)
            return new ShapeCreateCommand((ShapeModel)   request.getNewObject(),
                                          (DiagramModel) getHost().getModel(),
                                          (Rectangle)    getConstraintFor(request));
        return null;
    }

}