package com.fujitsu.us.visualvno.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import com.fujitsu.us.visualvno.ShapesPlugin;

/**
 * Abstract base of a shape
 * Has a size, a location, and a list of connections.
 */
public abstract class Shape extends ModelBase
{
    private static IPropertyDescriptor[] descriptors;
    private static final long serialVersionUID = 1;

    /** Property IDs **/
    public  static final String LOCATION_PROP   = "Shape.Location";
    public  static final String SIZE_PROP       = "Shape.Size";
    public  static final String SOURCE_PROP     = "Shape.SourceConnections";
    public  static final String TARGET_PROP     = "Shape.TargetConnections";
    public  static final String NAME_PROP       = "Shape.Name";
    
    private static final String HEIGHT_PROP     = "Shape.Height";
    private static final String WIDTH_PROP      = "Shape.Width";
    private static final String XPOS_PROP       = "Shape.X";
    private static final String YPOS_PROP       = "Shape.Y";
    
    private final Point     location = new Point(0, 0);
    private final Dimension size     = new Dimension(50, 50);
    private       String    name     = new String();
    private final List<Connection> sourceConnections = new ArrayList<Connection>();
    private final List<Connection> targetConnections = new ArrayList<Connection>();

    // initialize the proper descriptors
    static
    {
        descriptors = new IPropertyDescriptor[]
        {
            new TextPropertyDescriptor(XPOS_PROP,   "X"), 
            new TextPropertyDescriptor(YPOS_PROP,   "Y"),
            new TextPropertyDescriptor(WIDTH_PROP,  "Width"),
            new TextPropertyDescriptor(HEIGHT_PROP, "Height"),
            new TextPropertyDescriptor(NAME_PROP,   "Name"),
        };
        
        // use a custom cell editor validator for all four array entries
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
        
        ((PropertyDescriptor) descriptors[4]).setValidator(new ICellEditorValidator()
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

    /**
     * Utility method for generating a image from a file
     */
    protected static Image createImage(String fileName)
    {
        InputStream stream = ShapesPlugin.class.getResourceAsStream(fileName);
        Image image = new Image(null, stream);
        try {
            stream.close();
        }
        catch(IOException e)
        {}
        return image;
    }

    void addConnection(Connection connection)
    {
        if(connection == null || connection.getSource() == connection.getTarget())
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
    
    void removeConnection(Connection connection)
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
     * Return a pictogram (small icon) describing this model element.
     * @return a 16x16 Image or null
     */
    public abstract Image getIcon();

    public Point getLocation() {
        return location.getCopy();
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return descriptors;
    }

    /**
     * Return the property value for the given propertyId.
     * The property view uses the IDs from the IPropertyDescriptors array to
     * obtain the value of the corresponding properties.
     */
    @Override
    public Object getPropertyValue(Object propertyId)
    {
        if(XPOS_PROP.equals(propertyId))
            return Integer.toString(location.x);
        if(YPOS_PROP.equals(propertyId))
            return Integer.toString(location.y);
        if(HEIGHT_PROP.equals(propertyId))
            return Integer.toString(size.height);
        if(WIDTH_PROP.equals(propertyId))
            return Integer.toString(size.width);
        if(NAME_PROP.equals(propertyId))
            return getName();
        return super.getPropertyValue(propertyId);
    }
    
    /**
     * Set the property value for the given property id. If no matching id is
     * found, the call is forwarded to the superclass.
     */
    @Override
    public void setPropertyValue(Object propertyId, Object value)
    {
        if(XPOS_PROP.equals(propertyId))
        {
            int x = Integer.parseInt((String) value);
            setLocation(new Point(x, location.y));
        }
        else if(YPOS_PROP.equals(propertyId))
        {
            int y = Integer.parseInt((String) value);
            setLocation(new Point(location.x, y));
        }
        else if(HEIGHT_PROP.equals(propertyId))
        {
            int height = Integer.parseInt((String) value);
            setSize(new Dimension(size.width, height));
        }
        else if(WIDTH_PROP.equals(propertyId))
        {
            int width = Integer.parseInt((String) value);
            setSize(new Dimension(width, size.height));
        }
        else if(NAME_PROP.equals(propertyId)) {
            setName((String) value);
        }
        else {
            super.setPropertyValue(propertyId, value);
        }
    }

    public Dimension getSize() {
        return size.getCopy();
    }

    public void setSize(Dimension newSize)
    {
        if(newSize != null)
        {
            size.setSize(newSize);
            firePropertyChange(SIZE_PROP, null, size);
        }
    }
    
    public List<Connection> getSourceConnections() {
        return new ArrayList<Connection>(sourceConnections);
    }

    public List<Connection> getTargetConnections() {
        return new ArrayList<Connection>(targetConnections);
    }

    public void setLocation(Point newLocation)
    {
        if(newLocation == null)
            throw new IllegalArgumentException();
        location.setLocation(newLocation);
        firePropertyChange(LOCATION_PROP, null, location);
    }

    /**
     * Whether this and that are connected
     */
    public boolean connectsTo(Shape that)
    {
        if(this.equals(that))
            return true;

        for(Iterator<Connection> it = getSourceConnections().iterator(); it.hasNext();)
            if(it.next().getTarget().equals(that))
                return true;
        
        for(Iterator<Connection> it = getTargetConnections().iterator(); it.hasNext();)
            if(it.next().getSource().equals(that))
                return true;
        
        return false;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
        firePropertyChange(NAME_PROP, null, name);
    }
    
    @Override
    public String toString() {
        return getName();
    }
}
