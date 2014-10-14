package org.eclipse.gef.examples.shapes.parts.factories;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.examples.shapes.model.DiagramModel;
import org.eclipse.gef.examples.shapes.model.ShapeBase;
import org.eclipse.gef.examples.shapes.parts.DiagramTreeEditPart;
import org.eclipse.gef.examples.shapes.parts.ShapeTreeEditPart;

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
