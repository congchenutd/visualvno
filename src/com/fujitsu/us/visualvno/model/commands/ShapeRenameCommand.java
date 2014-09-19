package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.ShapeModel;

/**
 * A command to rename a shape
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class ShapeRenameCommand extends Command
{
    private final String        _newName;
    private final ShapeModel    _shape;
    private String              _oldName;

    public ShapeRenameCommand(ShapeModel shape, String newName)
    {
        _shape      = shape;
        _newName    = newName;
    }
    
    @Override
    public void execute()
    {
        _oldName = _shape.getName();  // backup
        redo();
    }
    
    @Override
    public void redo() {
        _shape.setName(_newName);
    }

    @Override
    public void undo() {
        _shape.setName(_oldName);
    }
}
