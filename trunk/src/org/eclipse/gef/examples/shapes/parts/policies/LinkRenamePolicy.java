package org.eclipse.gef.examples.shapes.parts.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.examples.shapes.figures.LinkConnection;
import org.eclipse.gef.examples.shapes.model.LinkBase;
import org.eclipse.gef.examples.shapes.model.commands.LinkRenameCommand;
import org.eclipse.gef.requests.DirectEditRequest;

/**
 * DirectEditPolicy for renaming Link
 * Generates LinkRenameCommand
 * Works with LabelCellEditorLocator and LabelDirectEditManager
 *  
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class LinkRenamePolicy extends DirectEditPolicy
{
    @Override
    protected Command getDirectEditCommand(DirectEditRequest request) {
        return new LinkRenameCommand(
                       (LinkBase) getHost().getModel(),
                       (String)   request.getCellEditor().getValue());
    }

    /**
     * Copies value from cell editor to figure
     */
    @Override
    protected void showCurrentEditValue(DirectEditRequest request)
    { 
        String value = (String) request.getCellEditor().getValue();
        ((LinkConnection) getHostFigure()).setText(value);
    }
}