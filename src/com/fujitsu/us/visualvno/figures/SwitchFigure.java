package com.fujitsu.us.visualvno.figures;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.EllipseAnchor;

public class SwitchFigure extends ShapeFigure
{
    private ConnectionAnchor _mappingAnchor;
    
    public SwitchFigure() {
        super(new Ellipse());
    }

    @Override
    public ConnectionAnchor getMappingAnchor()
    {
        if(_mappingAnchor == null)
            _mappingAnchor = new EllipseAnchor(this);
        return _mappingAnchor;
    }
}
