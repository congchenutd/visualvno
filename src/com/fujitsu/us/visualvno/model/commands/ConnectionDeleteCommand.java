package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.Connection;

/**
 * A command to disconnect (remove) a connection from its endpoints.
 */
public class ConnectionDeleteCommand extends Command
{

    /** Connection instance to disconnect. */
    private final Connection connection;

    public ConnectionDeleteCommand(Connection connection)
    {
        if(connection == null)
            throw new IllegalArgumentException();
        setLabel("Connection deletion");
        this.connection = connection;
    }

    @Override
    public void execute() {
        connection.disconnect();
    }

    @Override
    public void undo() {
        connection.reconnect();
    }
}
