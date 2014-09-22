package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.ShapeModel;

public class AddPortCommand extends Command
{
    private final ShapeModel _shape;
    
    public AddPortCommand(ShapeModel shape)
    {
        _shape = shape;
    }
    
    @Override
    public void execute()
    {
        redo();
    }
    
    @Override
    public void redo() {
    }

    @Override
    public void undo() {
    }
}
