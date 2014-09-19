package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.LinkModel;

/**
 * A command to disconnect (remove) a Link from its endpoints.
 */
public class LinkDeleteCommand extends Command
{
    private final LinkModel _connection;

    public LinkDeleteCommand(LinkModel connection)
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
