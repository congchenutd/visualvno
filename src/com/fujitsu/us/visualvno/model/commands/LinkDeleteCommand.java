package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.LinkModel;

/**
 * A command to disconnect (remove) a Link from its endpoints.
 */
public class LinkDeleteCommand extends Command
{
    private final LinkModel _link;

    public LinkDeleteCommand(LinkModel link)
    {
        if(link == null)
            throw new IllegalArgumentException();
        setLabel("Link deletion");
        _link = link;
    }

    @Override
    public void execute() {
        _link.disconnect();
    }

    @Override
    public void undo() {
        _link.reconnect();
    }
}
