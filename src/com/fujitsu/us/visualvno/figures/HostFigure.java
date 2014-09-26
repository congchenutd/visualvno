package com.fujitsu.us.visualvno.figures;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.RectangleFigure;

public class HostFigure extends ShapeFigure
{
    private ConnectionAnchor _mappingAnchor;
    
    public HostFigure() {
        super(new RectangleFigure());
    }

    @Override
    public ConnectionAnchor getMappingAnchor()
    {
        if(_mappingAnchor == null)
            _mappingAnchor = new ChopboxAnchor(this);
        return _mappingAnchor;
    }
}
