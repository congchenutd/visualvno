package com.fujitsu.us.visualvno.parts.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import com.fujitsu.us.visualvno.figures.ShapeFigure;
import com.fujitsu.us.visualvno.model.ShapeBase;
import com.fujitsu.us.visualvno.model.commands.ShapeRenameCommand;

/**
 * EditPolicy for renaming a shape
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class ShapeRenamePolicy extends DirectEditPolicy
{
    @Override
    protected Command getDirectEditCommand(DirectEditRequest request) {
        return new ShapeRenameCommand(
                      (ShapeBase) getHost().getModel(),
                      (String)    request.getCellEditor().getValue());
    }

    /**
     * Copies value from cell editor to figure
     */
    @Override
    protected void showCurrentEditValue(DirectEditRequest request)
    {
        String value = (String) request.getCellEditor().getValue();
        ((ShapeFigure) getHostFigure()).setText(value);
    }
}
