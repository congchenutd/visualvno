package org.eclipse.gef.examples.shapes.parts.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.examples.shapes.figures.ShapeFigure;
import org.eclipse.gef.examples.shapes.model.ShapeBase;
import org.eclipse.gef.examples.shapes.model.commands.ShapeRenameCommand;
import org.eclipse.gef.requests.DirectEditRequest;

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
                      (String)     request.getCellEditor().getValue());
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
