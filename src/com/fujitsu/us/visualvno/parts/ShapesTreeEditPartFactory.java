package com.fujitsu.us.visualvno.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.fujitsu.us.visualvno.model.Shape;
import com.fujitsu.us.visualvno.model.ShapesDiagram;

/**
 * Factory that maps model elements to TreeEditParts.
 * TreeEditParts are used in the outline view of the ShapesEditor.
 */
public class ShapesTreeEditPartFactory implements EditPartFactory
{
    @Override
    public EditPart createEditPart(EditPart context, Object model)
    {
        if(model instanceof Shape)
            return new ShapeTreeEditPart((Shape) model);
        if(model instanceof ShapesDiagram)
            return new DiagramTreeEditPart((ShapesDiagram) model);
        return null; // will not show an entry for the corresponding model
    }

}
