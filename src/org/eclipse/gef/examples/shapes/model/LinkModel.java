package org.eclipse.gef.examples.shapes.model;

/**
 * A link between two ports or a port and a host
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class LinkModel extends LinkBase
{
    private static final long  serialVersionUID  = 1;
    public static final String SMALL_IMAGE = "icons/Link.png";
    public static final String BIG_IMAGE   = "icons/Link.png";

    @Override
    public boolean canConnect(ILinkEnd source, ILinkEnd target)
    {
        // no self-self or duplicated link
        if(!super.canConnect(source, target))
            return false;
        
        // one end can not have multiple links
        for(LinkBase link: source.getAllLinks())
            if(!link.equals(this))
                return false;
        for(LinkBase link: target.getAllLinks())
            if(!link.equals(this))
                return false;
        
        // match types
        if(source instanceof SwitchModel)
            return false;
        if(source instanceof HostModel)
            return target instanceof PortModel || target instanceof PortModel;
        if(source instanceof PortModel)
            return target instanceof PortModel || target instanceof HostModel;
        return false;
    }

}