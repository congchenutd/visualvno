package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.ConnectionModel;

/**
 * A command to rename a connection
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class ConnectionRenameCommand extends Command
{
    private final ConnectionModel   _connection;
    private final String            _newName;
    private String                  _oldName;  // for undo

    public ConnectionRenameCommand(ConnectionModel connection, String newName)
    {
        _connection = connection;
        _newName    = newName;
    }
    
    @Override
    public void execute()
    {
        _oldName = _connection.getName();
        redo();
    }
    
    @Override
    public void redo() {
        _connection.setName(_newName);
    }

    @Override
    public void undo() {
        _connection.setName(_oldName);
    }

}