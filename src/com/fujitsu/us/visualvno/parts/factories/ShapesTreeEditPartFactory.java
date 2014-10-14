package com.fujitsu.us.visualvno.parts.factories;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.model.ShapeBase;
import com.fujitsu.us.visualvno.parts.DiagramTreeEditPart;
import com.fujitsu.us.visualvno.parts.ShapeTreeEditPart;

/**
 * A factory that maps models to edit parts.
 * Used by the outline view
 */
public class ShapesTreeEditPartFactory implements EditPartFactory
{

    @Override
    public EditPart createEditPart(EditPart context, Object model)
    {
        if(model instanceof DiagramModel)
            return new DiagramTreeEditPart((DiagramModel) model);
        if(model instanceof ShapeBase)
            return new ShapeTreeEditPart((ShapeBase) model);
        return null;
    }

}
