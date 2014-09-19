package com.fujitsu.us.visualvno.parts.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import com.fujitsu.us.visualvno.figures.ConnectionWithLabel;
import com.fujitsu.us.visualvno.model.LinkModel;
import com.fujitsu.us.visualvno.model.commands.LinkRenameCommand;

/**
 * DirectEditPolicy for renaming Link
 * Generates LinkRenameCommand
 * Works with LabelCellEditorLocator and LabelDirectEditManager
 *  
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class LinkRenameEditPolicy extends DirectEditPolicy
{
    @Override
    protected Command getDirectEditCommand(DirectEditRequest request) {
        return new LinkRenameCommand(
                       (LinkModel) getHost().getModel(),
                       (String)          request.getCellEditor().getValue());
    }

    @Override
    protected void showCurrentEditValue(DirectEditRequest request)
    {
        String value = (String) request.getCellEditor().getValue();
        ((ConnectionWithLabel) getHostFigure()).setText(value);
    }
}