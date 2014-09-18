package com.fujitsu.us.visualvno.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import com.fujitsu.us.visualvno.figures.LabeledFigureAdapter;
import com.fujitsu.us.visualvno.model.Shape;
import com.fujitsu.us.visualvno.model.commands.ShapeRenameCommand;

public class ShapeRenameEditPolicy extends DirectEditPolicy
{
    @Override
    protected Command getDirectEditCommand(DirectEditRequest request)
    {
        ShapeRenameCommand command = new ShapeRenameCommand();
        command.setModel((Shape) getHost().getModel());
        command.setNewName((String) request.getCellEditor().getValue());
        return command;
    }

    @Override
    protected void showCurrentEditValue(DirectEditRequest request)
    {
        String value = (String) request.getCellEditor().getValue();
        ((LabeledFigureAdapter) getHostFigure()).setText(value);
    }
}
