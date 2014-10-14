package com.fujitsu.us.visualvno.model;

import org.eclipse.draw2d.Graphics;

/**
 * A mapping "link" between a virtual node and physical node
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 *
 */
public class MappingModel extends LinkBase
{
    private static final long  serialVersionUID  = 1;
    public static final String SMALL_IMAGE = "icons/Mapping.png";
    public static final String BIG_IMAGE   = "icons/Mapping.png";
    
    public MappingModel()
    {
        super();
        setLineStyle(Graphics.LINE_DOT);
    }
    
    @Override
    public boolean canConnect(ILinkEnd source, ILinkEnd target)
    {
        // no self-self or duplicated link
        if(!super.canConnect(source, target))
            return false;
        
        // same type
        return source.getClass().equals(target.getClass());
    }
}
