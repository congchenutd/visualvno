package com.fujitsu.us.visualvno.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import com.fujitsu.us.visualvno.VisualVNOPlugin;

/**
 * Abstract base of a shape
 * Has a size, a location, and a list of connections.
 */
public abstract class ShapeModel extends ModelBase
{
    protected static IPropertyDescriptor[] descriptors;

    // property IDs
    public static final String LOCATION_PROP    = "Shape.Location";
    public static final String SIZE_PROP        = "Shape.Size";
    public static final String SOURCE_PROP      = "Shape.SourceConnections";
    public static final String TARGET_PROP      = "Shape.TargetConnections";
    public static final String NAME_PROP        = "Shape.Name";
    public static final String COLOR_PROP       = "Shape.Color";
    public static final String HEIGHT_PROP      = "Shape.Height";
    public static final String WIDTH_PROP       = "Shape.Width";
    public static final String XPOS_PROP        = "Shape.X";
    public static final String YPOS_PROP        = "Shape.Y";
    
    // properties
    private final Point     _location = new Point(0, 0);
    private final Dimension _size     = new Dimension(50, 50);
    private       String    _name     = new String();
    private       RGB       _color    = new RGB(0, 255, 0);
    
    private final List<ConnectionModel> sourceConnections = new ArrayList<ConnectionModel>();
    private final List<ConnectionModel> targetConnections = new ArrayList<ConnectionModel>();

    // initialize the property descriptors (for those appear in the property view)
    static
    {
        descriptors = new IPropertyDescriptor[]
        {
            new TextPropertyDescriptor (XPOS_PROP,   "X"), 
            new TextPropertyDescriptor (YPOS_PROP,   "Y"),
            new TextPropertyDescriptor (WIDTH_PROP,  "Width"),
            new TextPropertyDescriptor (HEIGHT_PROP, "Height"),
            new TextPropertyDescriptor (NAME_PROP,   "Name"),
            new ColorPropertyDescriptor(COLOR_PROP,  "Color")
        };
        
        // a custom cell editor validators
        for(int i = 0; i < 4; i++)
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
        
        ((PropertyDescriptor) descriptors[4]).setValidator(
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

    void addConnection(ConnectionModel connection)
    {
        if(connection == null)
            throw new IllegalArgumentException();
        
        if(connection.getSource() == this)
        {
            sourceConnections.add(connection);
            firePropertyChange(SOURCE_PROP, null, connection);
        }
        else if(connection.getTarget() == this)
        {
            targetConnections.add(connection);
            firePropertyChange(TARGET_PROP, null, connection);
        }
    }
    
    void removeConnection(ConnectionModel connection)
    {
        if(connection == null)
            throw new IllegalArgumentException();
        
        if(connection.getSource() == this)
        {
            sourceConnections.remove(connection);
            firePropertyChange(SOURCE_PROP, null, connection);
        }
        else if(connection.getTarget() == this)
        {
            targetConnections.remove(connection);
            firePropertyChange(TARGET_PROP, null, connection);
        }
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
    
    public List<ConnectionModel> getSourceConnections() {
        return new ArrayList<ConnectionModel>(sourceConnections);
    }

    public List<ConnectionModel> getTargetConnections() {
        return new ArrayList<ConnectionModel>(targetConnections);
    }

    /**
     * Whether this and that are connected
     */
    public boolean connectsTo(ShapeModel that)
    {
        if(this.equals(that))
            return true;

        for(Iterator<ConnectionModel> it = getSourceConnections().iterator(); it.hasNext();)
            if(it.next().getTarget().equals(that))
                return true;
        
        for(Iterator<ConnectionModel> it = getTargetConnections().iterator(); it.hasNext();)
            if(it.next().getSource().equals(that))
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
