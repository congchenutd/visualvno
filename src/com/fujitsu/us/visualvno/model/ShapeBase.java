package com.fujitsu.us.visualvno.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;

/**
 * Base class for all the shapes
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public abstract class ShapeBase extends ModelBase
{
    private static final long serialVersionUID = 1;
    protected static IPropertyDescriptor[] _descriptors;
    
    // property IDs
    public static final String SOURCE_LINKS_PROP = "ShapeModel.SourceLinks";
    public static final String TARGET_LINKS_PROP = "ShapeModel.TargetLinks";
    public static final String LOCATION_PROP     = "ShapeModel.Location";
    public static final String SIZE_PROP         = "ShapeModel.Size";
    public static final String COLOR_PROP        = "ShapeModel.Color";
    public static final String NAME_PROP         = "ShapeModel.Name";
    public static final String VNOID_PROP        = "ShapeModel.VnoID";

    // init property descriptors
    static
    {
        _descriptors = new IPropertyDescriptor[] {
            new PropertyDescriptor      (LOCATION_PROP, "Location"),
            new PropertyDescriptor      (SIZE_PROP,     "Size"),
            new TextPropertyDescriptor  (NAME_PROP,     "Name"),
            new TextPropertyDescriptor  (VNOID_PROP,    "VNO ID"),
            new ColorPropertyDescriptor (COLOR_PROP,    "Color")
        };
    }

    // properties
    private Point     _location = new Point(0, 0);
    private Dimension _size     = new Dimension(50, 50);
    private String    _name     = new String();
    private RGB       _color    = new RGB(0, 255, 0);
    private List<LinkBase> _sourceLinks = new ArrayList<LinkBase>();
    private List<LinkBase> _targetLinks = new ArrayList<LinkBase>();
    
    public List<LinkBase> getSourceLinks() {
        return new ArrayList<LinkBase>(_sourceLinks);
    }

    public List<LinkBase> getTargetLinks() {
        return new ArrayList<LinkBase>(_targetLinks);
    }
    
    public void addLink(LinkBase link)
    {
        if(link == null || link.getSource() == link.getTarget())
            throw new IllegalArgumentException();

        if(link.getSource() == this)
        {
            _sourceLinks.add(link);
            firePropertyChange(SOURCE_LINKS_PROP, null, link);
        }
        else if(link.getTarget() == this)
        {
            _targetLinks.add(link);
            firePropertyChange(TARGET_LINKS_PROP, null, link);
        }
    }
    
    public void removeLink(LinkBase link)
    {
        if (link == null)
            throw new IllegalArgumentException();

        if (link.getSource() == this)
        {
            _sourceLinks.remove(link);
            firePropertyChange(SOURCE_LINKS_PROP, null, link);
        }
        else if (link.getTarget() == this)
        {
            _targetLinks.remove(link);
            firePropertyChange(TARGET_LINKS_PROP, null, link);
        }
    }
    
    /**
     * Remove all the links
     */
    public void clearLinks() {
        for(LinkBase link: getAllLinks())
            link.disconnect();
    }
    
    public List<LinkBase> getAllLinks()
    {
        List<LinkBase> result = new ArrayList<LinkBase>(getSourceLinks());
        result.addAll(getTargetLinks());
        return result;
    }
    
    /**
     * Whether this and that are connected by any link
     */
    public boolean connectsTo(ILinkEnd that)
    {
        if(this.equals(that))
            return true;

        for(LinkBase link: getAllLinks())
            if(link.getTarget().equals(that))
                return true;
        
        return false;
    }
    
    public Dimension getSize() {
        return _size.getCopy();
    }

    public void setSize(Dimension newSize)
    {
        if(newSize == null)
            throw new IllegalArgumentException();
        
        // keep being circle or square
        int oldWidth  = _size.width();
        int oldHeight = _size.height();
        int newWidth  = newSize.width();
        int newHeight = newSize.height();
        
        // one dimension is changed, update both
        if(oldWidth == newWidth)
            _size.setSize(newHeight, newHeight);
        else if(oldHeight == newHeight)
            _size.setSize(newWidth, newWidth);
        
        // two dimensions changed, use the max
        else
        {
            int width = Math.max(newSize.width, newSize.height);
            _size.setSize(width, width);
        }
        firePropertyChange(SIZE_PROP, null, _size);
    }
    
    public Point getLocation() {
        return _location.getCopy();
    }
    
    public void setLocation(Point newLocation)
    {
        if(newLocation == null)
            throw new IllegalArgumentException();
        
        _location.setLocation(newLocation);
        firePropertyChange(LOCATION_PROP, null, _location);
    }
    
    public String getName() {
        return _name;
    }
    
    public void setName(String name)
    {
        _name = name;
        firePropertyChange(NAME_PROP, null, _location);
    }
    
    public RGB getColor() {
        return _color;
    }

    public void setColor(RGB color)
    {
        if(color == null)
            throw new IllegalArgumentException();
        
        _color = color;
        firePropertyChange(COLOR_PROP, null, color);
    }
    
    public abstract Image getIcon();

    /**
     * Utility method for generating a image from a file
     */
    protected static Image createImage(String name)
    {
        InputStream stream = VisualVNOPlugin.class.getResourceAsStream(name);
        Image image = new Image(null, stream);
        try {
            stream.close();
        }
        catch(IOException ioe) {}
        return image;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return _descriptors;
    }

    @Override
    public Object getPropertyValue(Object id)
    {
        if(LOCATION_PROP.equals(id))
            return new LocationPropertySource(getLocation());
        if(SIZE_PROP.equals(id))
            return new DimensionPropertySource(getSize());
        if(NAME_PROP.equals(id))
            return _name;
        if(COLOR_PROP.equals(id))
            return getColor();
        if(VNOID_PROP.equals(id))
            return Integer.toString(getVNOID());
        return super.getPropertyValue(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value)
    {
        if(LOCATION_PROP.equals(id))
            setLocation((Point) value);
        else if(SIZE_PROP.equals(id))
            setSize((Dimension) value);
        else if(NAME_PROP.equals(id))
            setName((String) value);
        else if(COLOR_PROP.equals(id))
            setColor((RGB) value);
        else if(VNOID_PROP.equals(id))
            setVNOID(Integer.parseInt((String) value));
        else
            super.setPropertyValue(id, value);
    }

}