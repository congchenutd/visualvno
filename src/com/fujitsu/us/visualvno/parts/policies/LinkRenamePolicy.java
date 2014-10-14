package com.fujitsu.us.visualvno.parts.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import com.fujitsu.us.visualvno.figures.LinkConnection;
import com.fujitsu.us.visualvno.model.LinkBase;
import com.fujitsu.us.visualvno.model.commands.LinkRenameCommand;

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