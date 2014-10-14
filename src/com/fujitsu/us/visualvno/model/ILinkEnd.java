package com.fujitsu.us.visualvno.model;

import java.util.List;

/**
 * Interface for a shape that can be connected, 
 * such as SwitchModel, PortModel and HostModel
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public interface ILinkEnd
{
    /** Add a link to this end */
    void addLink(LinkBase link);
    
    /** Remove a link from this end */
    void removeLink(LinkBase link);
    
    /** Get all the links of this end */
    List<LinkBase> getAllLinks();
    
    /** Test if this and end are connected */
    boolean connectsTo(ILinkEnd end);
}
