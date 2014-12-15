package com.fujitsu.us.visualvno.model;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;


/**
 * The diagram hosting all other shapes
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class DiagramModel extends ContainerModel
{
    private static final long  serialVersionUID = 1;
    
    private boolean _gridEnabled;
    private boolean _snapToGeometry;
    private double  _zoomFactor;
    
    public static final String FONTSIZE_PROP = "DiagramModel.FontSize";
    protected static IPropertyDescriptor[] _descriptors;
    
    // init property descriptors
    static
    {
        _descriptors = new IPropertyDescriptor[] {
            new TextPropertyDescriptor  (VNOID_PROP,    "VNO ID"),
            new TextPropertyDescriptor  (FONTSIZE_PROP, "Font size"),
            new ColorPropertyDescriptor (COLOR_PROP,    "Color")
        };
    }
    
    private int _fontSize = 14;
    public static final int MIN_FONT_SIZE = 8; 

    public DiagramModel() {
        setName("VNO Diagram");
    }
    
    public boolean isGridEnabled() {
        return _gridEnabled;
    }

    public void setGridEnabled(boolean isEnabled) {
        _gridEnabled = isEnabled;
    }
    
    public boolean isSnapToGeometryEnabled() {
        return _snapToGeometry;
    }

    public void setSnapToGeometry(boolean isEnabled) {
        _snapToGeometry = isEnabled;
    }

    public double getZoom() {
        return _zoomFactor;
    }
    
    public void setZoom(double zoom) {
        _zoomFactor = zoom;
    }
    
    public int getFontSize() {
        return _fontSize;
    }
    
    public Font getFont() {
        return new Font(null, FONT_NAME, _fontSize, SWT.NORMAL);
    }
    
    public void setFontSize(int size)
    {
        if(size >= MIN_FONT_SIZE)
        {
            _fontSize = size; 
            firePropertyChange(FONTSIZE_PROP, null, size);
        }
    }
    
    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return _descriptors;
    }

    @Override
    public Object getPropertyValue(Object id)
    {
        if(FONTSIZE_PROP.equals(id))
            return Integer.toString(getFontSize());
        return super.getPropertyValue(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value)
    {
        if(FONTSIZE_PROP.equals(id))
            setFontSize(Integer.parseInt((String) value));
        else
            super.setPropertyValue(id, value);
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
    // the following are for demo only
    public void removeNetwork(int vnoID) {
        new Network(this, vnoID).removeFrom(this);
    }
    
    public void addNetwork(Network network)
    {
        removeNetwork(network.getVNOID());
        network.addTo(this);
    }

}
