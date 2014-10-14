package com.fujitsu.us.visualvno.model;

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
    
    
    @Override
    public String toString() {
        return getName();
    }
}
