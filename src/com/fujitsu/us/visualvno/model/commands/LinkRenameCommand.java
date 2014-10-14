package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.LinkBase;

/**
 * A command to rename a Link
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class LinkRenameCommand extends Command
{
    private final LinkBase  _link;
    private final String    _newName;
    private String          _oldName;  // for undo

    public LinkRenameCommand(LinkBase link, String newName)
    {
        _link       = link;
        _newName    = newName;
    }
    
    @Override
    public void execute()
    {
        _oldName = _link.getName();
        redo();
    }
    
    @Override
    public void redo() {
        _link.setName(_newName);
    }

    @Override
    public void undo() {
        _link.setName(_oldName);
    }

}