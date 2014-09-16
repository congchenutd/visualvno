package com.fujitsu.us.visualvno.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.fujitsu.us.visualvno.model.Connection;
import com.fujitsu.us.visualvno.model.Shape;
import com.fujitsu.us.visualvno.model.ShapesDiagram;

/**
 * Factory that maps model elements to edit parts.
 */
public class ShapesEditPartFactory implements EditPartFactory
{
    @Override
    public EditPart createEditPart(EditPart context, Object modelElement)
    {
        // get EditPart for model element
        EditPart part = getPartForElement(modelElement);

        // store model element in EditPart
        part.setModel(modelElement);
        return part;
    }

    /**
     * Maps an object to an EditPart.
     */
    private EditPart getPartForElement(Object modelElement)
    {
        if(modelElement instanceof ShapesDiagram)
            return new DiagramEditPart();
        if(modelElement instanceof Shape)
            return new ShapeEditPart();
        if(modelElement instanceof Connection)
            return new ConnectionEditPart();
        throw new RuntimeException("Can't create part for model element: "
                + ((modelElement != null) ? modelElement.getClass().getName() 
                                          : "null"));
    }

}
