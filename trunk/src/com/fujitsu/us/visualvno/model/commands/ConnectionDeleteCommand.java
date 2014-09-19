package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.ConnectionModel;

/**
 * A command to disconnect (remove) a connection from its endpoints.
 */
public class ConnectionDeleteCommand extends Command
{
    private final ConnectionModel _connection;

    public ConnectionDeleteCommand(ConnectionModel connection)
    {
        if(connection == null)
            throw new IllegalArgumentException();
        setLabel("Connection deletion");
        _connection = connection;
    }

    @Override
    public void execute() {
        _connection.disconnect();
    }

    @Override
    public void undo() {
        _connection.reconnect();
    }
}
