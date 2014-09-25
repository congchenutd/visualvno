package com.fujitsu.us.visualvno.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;

/**
 * Abstract base of a shape
 * Has a size, a location, and a list of connections.
 */
public abstract class ShapeModel extends ModelBase
{
	private static final long serialVersionUID = 1L;
	
    protected static IPropertyDescriptor[] descriptors;

    // property IDs
    public static final String LOCATION_PROP    = "Shape.Location";
    public static final String SIZE_PROP        = "Shape.Size";
    public static final String SOURCELINK_PROP  = "Shape.SourceLinks";
    public static final String TARGETLINK_PROP  = "Shape.TargetLinks";
    public static final String NAME_PROP        = "Shape.Name";
    public static final String PORTCOUNT_PROP   = "Shape.PortCount";
    public static final String COLOR_PROP       = "Shape.Color";
    public static final String HEIGHT_PROP      = "Shape.Height";
    public static final String WIDTH_PROP       = "Shape.Width";
    public static final String XPOS_PROP        = "Shape.X";
    public static final String YPOS_PROP        = "Shape.Y";
    
    // properties
    private final Point     _location   = new Point(0, 0);
    private final Dimension _size       = new Dimension(50, 50);
    private       String    _name       = new String();
    private       RGB       _color      = new RGB(0, 255, 0);
    
    private final List<Port> _ports = new ArrayList<Port>();

    // initialize the property descriptors (for those appear in the property view)
    static
    {
        descriptors = new IPropertyDescriptor[]
        {
            new TextPropertyDescriptor (XPOS_PROP,      "X"), 
            new TextPropertyDescriptor (YPOS_PROP,      "Y"),
            new TextPropertyDescriptor (WIDTH_PROP,     "Width"),
            new TextPropertyDescriptor (HEIGHT_PROP,    "Height"),
            new TextPropertyDescriptor (PORTCOUNT_PROP, "PortCount"),
            new TextPropertyDescriptor (NAME_PROP,      "Name"),
            new ColorPropertyDescriptor(COLOR_PROP,     "Color"),
        };
        
        // a custom cell editor validator
        for(int i = 0; i < 5; i++)
        {
            PropertyDescriptor descriptor = (PropertyDescriptor) descriptors[i];
            descriptor.setValidator(
                new ICellEditorValidator()
                {
                    @Override
                    public String isValid(Object value)
                    {
                        int intValue = -1;
                        try {
                            intValue = Integer.parseInt((String) value);
                        }
                        catch(NumberFormatException exc) {
                            return "Not a number";
                        }
                        return (intValue >= 0) ? null 
                                               : "Value must be >=  0";
                    }
                });
        }
        
        ((PropertyDescriptor) descriptors[5]).setValidator(
           new ICellEditorValidator()
           {
               @Override
               public String isValid(Object value)
               {
                   String name = (String) value;
                   return name != null && !name.isEmpty() ? null 
                                                          : "Name can't be empty";
               }
           });
    } // static
    
    public void updateLink(LinkModel link)
    {
        if(link == null)
            throw new IllegalArgumentException();
        
        if(link.getSourceShape() == this)
            firePropertyChange(SOURCELINK_PROP, null, link);
        else if(link.getTargetShape() == this)
            firePropertyChange(TARGETLINK_PROP, null, link);
    }

    /**
     * Return a 16x16 icon for this model element.
     */
    public abstract Image getIcon();

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return descriptors;
    }

    /**
     * Return the property value for the given propertyId.
     * The property view uses the IDs from the IPropertyDescriptors array to
     * obtain the value of the corresponding property values.
     */
    @Override
    public Object getPropertyValue(Object id)
    {
        if(XPOS_PROP.equals(id))
            return Integer.toString(_location.x);
        if(YPOS_PROP.equals(id))
            return Integer.toString(_location.y);
        if(HEIGHT_PROP.equals(id))
            return Integer.toString(_size.height);
        if(WIDTH_PROP.equals(id))
            return Integer.toString(_size.width);
        if(NAME_PROP.equals(id))
            return getName();
        if(PORTCOUNT_PROP.equals(id))
            return Integer.toString(getPortCount());
        if(COLOR_PROP.equals(id))
            return getColor();
        return super.getPropertyValue(id);
    }
    
    /**
     * Set the property value for the given property id.
     */
    @Override
    public void setPropertyValue(Object id, Object value)
    {
        if(XPOS_PROP.equals(id))
        {
            int x = Integer.parseInt((String) value);
            setLocation(new Point(x, _location.y));
        }
        else if(YPOS_PROP.equals(id))
        {
            int y = Integer.parseInt((String) value);
            setLocation(new Point(_location.x, y));
        }
        else if(HEIGHT_PROP.equals(id))
        {
            int height = Integer.parseInt((String) value);
            setSize(new Dimension(_size.width, height));
        }
        else if(WIDTH_PROP.equals(id))
        {
            int width = Integer.parseInt((String) value);
            setSize(new Dimension(width, _size.height));
        }
        else if(NAME_PROP.equals(id)) {
            setName((String) value);
        }
        else if(PORTCOUNT_PROP.equals(id)) {
            setPortCount(Integer.parseInt((String) value));
        }
        else if(COLOR_PROP.equals(id)) {
            setColor((RGB) value);
        }
        else {
            super.setPropertyValue(id, value);
        }
    }

    public Dimension getSize() {
        return _size.getCopy();
    }

    public void setSize(Dimension newSize)
    {
        if(newSize == null || newSize.isEmpty())
            throw new IllegalArgumentException();
        
        _size.setSize(newSize);
        firePropertyChange(SIZE_PROP, null, _size);
    }
    
    public String getName() {
        return _name;
    }
    
    public void setName(String name)
    {
        if(name == null)
            throw new IllegalArgumentException();
        
        _name = name;
        firePropertyChange(NAME_PROP, null, name);
    }
    
    public int getPortCount() {
        return _ports.size();
    }
    
    public void setPortCount(int count)
    {
        if(count < 1)
            throw new IllegalArgumentException();
        
        _ports.clear();
        for(int i = 0; i < count; ++i)
            _ports.add(new Port(this, i + 1));
            
        firePropertyChange(PORTCOUNT_PROP, null, count);
    }
    
    public Port getPort(int portNumber) {
        return _ports.isEmpty() ? null : _ports.get(portNumber - 1);
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
    
    public List<LinkModel> getSourceLinks()
    {
        List<LinkModel> links = new ArrayList<LinkModel>();
        for(Port port: _ports)
            if(port.getLink() != null && port.getLink().getSourcePort().equals(port))
                links.add(port.getLink());
        return links;
    }

    public List<LinkModel> getTargetLinks()
    {
        List<LinkModel> links = new ArrayList<LinkModel>();
        for(Port port: _ports)
            if(port.getLink() != null && port.getLink().getTargetPort().equals(port))
                links.add(port.getLink());
        return links;
    }

    /**
     * Whether this and that are connected
     */
    public boolean connectsTo(ShapeModel that)
    {
        if(this.equals(that))
            return true;

        for(Iterator<LinkModel> it = getSourceLinks().iterator(); it.hasNext();)
            if(it.next().getTargetShape().equals(that))
                return true;
        
        for(Iterator<LinkModel> it = getTargetLinks().iterator(); it.hasNext();)
            if(it.next().getSourceShape().equals(that))
                return true;
        
        return false;
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
    /**
     * Utility method for generating a image from a file
     */
    protected static Image createImage(String fileName)
    {
        InputStream stream = VisualVNOPlugin.class.getResourceAsStream(fileName);
        Image image = new Image(null, stream);
        try {
            stream.close();
        }
        catch(IOException e)
        {}
        return image;
    }

}
