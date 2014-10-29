package com.fujitsu.us.visualvno.figures;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

/**
 * A polyline connection with an editable label
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class LinkConnection extends PolylineConnection
{
    private final Label _label;

    public LinkConnection()
    {
        _label = new Label("Link");
        
        // put the label in the middle of the connection
        add(_label, new MidpointLocator(this, 0));
        
        setFont(new Font(null, "Arial", 14, SWT.NORMAL));
    }

    public Label getLabel() {
        return _label;
    }
    
    public void setText(String text) {
        _label.setText(text);
    }
}