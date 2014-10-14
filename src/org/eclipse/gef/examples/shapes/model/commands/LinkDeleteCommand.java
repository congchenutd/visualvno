package org.eclipse.gef.examples.shapes.model.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.shapes.model.LinkBase;

/**
 * Delete a link
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class LinkDeleteCommand extends Command
{
    private final LinkBase _link;

    public LinkDeleteCommand(LinkBase link)
    {
        if(link == null)
            throw new IllegalArgumentException();

        setLabel("Connection deletion");
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
