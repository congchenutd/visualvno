package com.fujitsu.us.visualvno.model.commands;

import org.eclipse.gef.commands.Command;

import com.fujitsu.us.visualvno.model.Connection;
import com.fujitsu.us.visualvno.model.Shape;

/**
 * A command to reconnect a connection to a different start point or end point.
 */
public class ConnectionReconnectCommand extends Command
{

    /** The connection instance to reconnect. */
    private final Connection  connection;
    
    private         Shape newSource;
    private         Shape newTarget;
    private final   Shape oldSource;
    private final   Shape oldTarget;

    public ConnectionReconnectCommand(Connection connection)
    {
        if(connection == null)
            throw new IllegalArgumentException();
        
        this.connection = connection;
        this.oldSource  = connection.getSource();
        this.oldTarget  = connection.getTarget();
    }

    @Override
    public boolean canExecute()
    {
        if(newSource != null)
            return !newSource.connectsTo(oldTarget);
        else if(newTarget != null)
            return !newTarget.connectsTo(oldSource);
        return false;
    }

    /**
     * Reconnect the connection to newSource (if setNewSource(...) was invoked before)
     * or newTarget (if setNewTarget(...) was invoked before).
     */
    @Override
    public void execute()
    {
        if(newSource != null)
            connection.reconnect(newSource, oldTarget);
        else if(newTarget != null)
            connection.reconnect(oldSource, newTarget);
        else
            throw new IllegalStateException("Should not happen");
    }

    /**
     * Set a new source endpoint for this connection. When execute() is invoked,
     * the source endpoint of the connection will be attached to the supplied
     * Shape instance.
     * 
     * Note: Calling this method, deactivates reconnection of the target
     * endpoint. A single instance of this command can only reconnect either the
     * source or the target endpoint.
     */
    public void setNewSource(Shape connectionSource)
    {
        if(connectionSource == null)
            throw new IllegalArgumentException();

        setLabel("Move connection startpoint");
        newSource = connectionSource;
        newTarget = null;
    }

    /**
     * Set a new target endpoint for this connection When execute() is invoked,
     * the target endpoint of the connection will be attached to the supplied
     * Shape instance.
     *
     * Note: Calling this method, deactivates reconnection of the source
     * endpoint. A single instance of this command can only reconnect either the
     * source or the target endpoint.
     */
    public void setNewTarget(Shape connectionTarget)
    {
        if(connectionTarget == null)
            throw new IllegalArgumentException();

        setLabel("Move connection endpoint");
        newSource = null;
        newTarget = connectionTarget;
    }

    @Override
    public void undo() {
        connection.reconnect(oldSource, oldTarget);
    }

}
