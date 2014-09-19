package com.fujitsu.us.visualvno.parts.factories;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.fujitsu.us.visualvno.model.LinkModel;
import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.parts.LinkEditPart;
import com.fujitsu.us.visualvno.parts.DiagramEditPart;
import com.fujitsu.us.visualvno.parts.ShapeEditPart;

/**
 * Factory that maps model elements to edit parts.
 */
public class ShapesEditPartFactory implements EditPartFactory
{
    @Override
    public EditPart createEditPart(EditPart context, Object modelElement)
    {
        EditPart part = getPartForElement(modelElement);
        part.setModel(modelElement);
        return part;
    }

    /**
     * Maps an model to an EditPart.
     */
    private EditPart getPartForElement(Object modelElement)
    {
        if(modelElement instanceof DiagramModel)
            return new DiagramEditPart();
        if(modelElement instanceof ShapeModel)
            return new ShapeEditPart();
        if(modelElement instanceof LinkModel)
            return new LinkEditPart();
        throw new RuntimeException(
               "Can't create part for model element: " + 
               ((modelElement != null) ? modelElement.getClass().getName() 
                                       : "null"));
    }

}
