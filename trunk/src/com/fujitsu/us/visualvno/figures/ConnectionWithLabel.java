package com.fujitsu.us.visualvno.figures;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolylineConnection;

/**
 * A polyline connection with an editable label
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class ConnectionWithLabel extends PolylineConnection
{
    private final Label _label;

    public ConnectionWithLabel()
    {
        _label = new Label("Connection");
        
        // put the label in the middle of the connection
        add(_label, new MidpointLocator(this, 0));
    }

    public Label getLabel() {
        return _label;
    }
    
    public void setText(String text) {
        _label.setText(text);
    }
}