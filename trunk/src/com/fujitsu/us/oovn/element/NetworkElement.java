package com.fujitsu.us.oovn.element;

import com.fujitsu.us.oovn.map.MapBase;

/**
 * The interface for all network elements that can be saved in Neo4j
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public interface NetworkElement
{
    /**
     * @return a variable name for Neo4J query, without ()
     */
    public String toDBVariable();
    
    /**
     * @return a Neo4j clause for matching the node, including ()
     * Should include all the information of the node,
     * so that it can also be used for creating the node
     */
    public String toDBMatch();
    
    /**
     * Create the node(s) (network, switch, link, port) in the db
     * @param engine execution engine of Neo4j
     */
    public void createInDB(MapBase map);
    
    /**
     * Create the mapping to the physical node(s) in the db
     * @param engine execution engine of Neo4j
     */
    public void createMapping(MapBase map);
}
