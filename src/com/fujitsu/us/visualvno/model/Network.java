package com.fujitsu.us.visualvno.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of shapes and links belonging to a network
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class Network
{
    private int             _vnoID;
    private List<ShapeBase> _shapes = new ArrayList<ShapeBase>();
    private List<LinkBase>  _links  = new ArrayList<LinkBase>();
    
    public Network(DiagramModel diagram, int vnoID)
    {
        _vnoID = vnoID;
        for(ShapeBase child: diagram.getChildren())
            if(child.getVNOID() == vnoID)
            {
                addShape(child);
                collectLinks(child);
            }
    }
    
    public int getVNOID() {
        return _vnoID;
    }
    
    public void addShape(ShapeBase shape) {
        _shapes.add(shape);
    }
    
    public void addLinks(List<LinkBase> links) {
        _links.addAll(links);
    }
    
    public List<ShapeBase> getShapes() {
        return _shapes;
    }
    
    public List<LinkBase> getLinks() {
        return _links;
    }
    
    /**
     * Remove the network from the diagram
     */
    public void removeFrom(DiagramModel diagram)
    {
        for(ShapeBase child: getShapes())
            diagram.removeChild(child);
        for(LinkBase link: getLinks())
            link.disconnect();
    }
    
    /**
     * Add the network to the diagram
     */
    public void addTo(DiagramModel diagram)
    {
        for(ShapeBase child: getShapes())
            diagram.addChild(child);
        for(LinkBase link: getLinks())
            link.reconnect();
    }
    
    private void collectLinks(ShapeBase shape)
    {
        // links of the shape
        addLinks(shape.getSourceLinks());
        addLinks(shape.getTargetLinks());
        
        // links of its children
        if(shape instanceof ContainerModel)
        {
            ContainerModel container = (ContainerModel) shape;
            for(ShapeBase child: container.getChildren())
                collectLinks(child);
        }
    }
}
