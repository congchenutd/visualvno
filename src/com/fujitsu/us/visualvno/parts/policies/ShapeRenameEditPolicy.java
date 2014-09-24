package com.fujitsu.us.visualvno.parts.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import com.fujitsu.us.visualvno.figures.LabeledShape;
import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.model.commands.ShapeRenameCommand;

/**
 * EditPolicy for renaming a shape
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class ShapeRenameEditPolicy extends DirectEditPolicy
{
    @Override
    protected Command getDirectEditCommand(DirectEditRequest request) {
        return new ShapeRenameCommand(
                      (ShapeModel) getHost().getModel(),
                      (String)     request.getCellEditor().getValue());
    }

    @Override
    protected void showCurrentEditValue(DirectEditRequest request)
    {
        String value = (String) request.getCellEditor().getValue();
        ((LabeledShape) getHostFigure()).setText(value);
    }
}
