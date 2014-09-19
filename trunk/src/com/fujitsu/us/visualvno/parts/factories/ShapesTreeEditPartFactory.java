package com.fujitsu.us.visualvno.parts.factories;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.parts.DiagramTreeEditPart;
import com.fujitsu.us.visualvno.parts.ShapeTreeEditPart;

/**
 * Factory that maps model elements to TreeEditParts.
 * TreeEditParts are used in the outline view
 */
public class ShapesTreeEditPartFactory implements EditPartFactory
{
    @Override
    public EditPart createEditPart(EditPart context, Object model)
    {
        if(model instanceof ShapeModel)
            return new ShapeTreeEditPart((ShapeModel) model);
        if(model instanceof DiagramModel)
            return new DiagramTreeEditPart((DiagramModel) model);
        return null; // will not show an entry for the corresponding model
    }

}
