package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.Shape;

public class ShapeRenameCommand extends Command
{

    private String oldName;
    private String newName;
    private Shape  model;

    @Override
    public void execute()
    {
        oldName = model.getName();
        model.setName(newName);
    }

    @Override
    public void undo() {
        model.setName(oldName);
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public void setModel(Shape model) {
        this.model = model;
    }
}
