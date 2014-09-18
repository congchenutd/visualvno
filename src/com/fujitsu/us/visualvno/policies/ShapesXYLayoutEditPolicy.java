package com.fujitsu.us.visualvno.policies;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.fujitsu.us.visualvno.model.Diagram;
import com.fujitsu.us.visualvno.model.Host;
import com.fujitsu.us.visualvno.model.Shape;
import com.fujitsu.us.visualvno.model.Switch;
import com.fujitsu.us.visualvno.model.commands.ShapeCreateCommand;
import com.fujitsu.us.visualvno.model.commands.ShapeSetConstraintCommand;
import com.fujitsu.us.visualvno.parts.ShapeEditPart;

/**
 * EditPolicy for the Figure used by this edit part. Children of
 * XYLayoutEditPolicy can be used in Figures with XYLayout.
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
            return new ShapeSetConstraintCommand((Shape) child.getModel(), 
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
        if(childClass == Switch.class || 
           childClass == Host.class)
        {
            // return a command that can add a Shape to a ShapesDiagram
            return new ShapeCreateCommand((Shape) request.getNewObject(),
                                          (Diagram) getHost().getModel(),
                                          (Rectangle) getConstraintFor(request));
        }
        return null;
    }

}