package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.Connection;
import com.fujitsu.us.visualvno.model.Shape;

/**
 * A command to create a connection between two shapes.
 */
public class ConnectionCreateCommand extends Command
{
    /** The connection instance. */
    private Connection  connection;
    
    /** The desired line style for the connection (dashed or solid). */
    private final int   lineStyle;

    /** Start endpoint for the connection. */
    private final Shape source;
    
    /** Target endpoint for the connection. */
    private Shape       target;

    public ConnectionCreateCommand(Shape source, int lineStyle)
    {
        if(source == null)
            throw new IllegalArgumentException();

        setLabel("Connection creation");
        this.source    = source;
        this.lineStyle = lineStyle;
    }

    @Override
    public boolean canExecute() {
        return !source.connectsTo(target);
    }

    @Override
    public void execute()
    {
        connection = new Connection(source, target);
        connection.setLineStyle(lineStyle);
    }

    @Override
    public void redo() {
        connection.reconnect();
    }

    public void setTarget(Shape target)
    {
        if(target == null)
            throw new IllegalArgumentException();
        this.target = target;
    }

    @Override
    public void undo() {
        connection.disconnect();
    }
}
