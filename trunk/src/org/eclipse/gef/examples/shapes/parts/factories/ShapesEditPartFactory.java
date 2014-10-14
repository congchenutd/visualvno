package org.eclipse.gef.examples.shapes.parts.factories;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.examples.shapes.model.DiagramModel;
import org.eclipse.gef.examples.shapes.model.HostModel;
import org.eclipse.gef.examples.shapes.model.LinkBase;
import org.eclipse.gef.examples.shapes.model.PortModel;
import org.eclipse.gef.examples.shapes.model.SwitchModel;
import org.eclipse.gef.examples.shapes.parts.DiagramEditPart;
import org.eclipse.gef.examples.shapes.parts.HostEditPart;
import org.eclipse.gef.examples.shapes.parts.LinkEditPart;
import org.eclipse.gef.examples.shapes.parts.PortEditPart;
import org.eclipse.gef.examples.shapes.parts.SwitchEditPart;

/**
 * A factory that maps models to edit parts.
 */
public class ShapesEditPartFactory implements EditPartFactory
{

    @Override
    public EditPart createEditPart(EditPart context, Object modelElement)
    {
        EditPart part;
        if(modelElement instanceof DiagramModel)
            part = new DiagramEditPart();
        else if(modelElement instanceof SwitchModel)
            part = new SwitchEditPart();
        else if(modelElement instanceof HostModel)
            part = new HostEditPart();
        else if(modelElement instanceof PortModel)
            part = new PortEditPart();
        else if(modelElement instanceof LinkBase)
            part = new LinkEditPart();
        else
        throw new RuntimeException("Can't create part for model element: "
                + ((modelElement != null) ? modelElement.getClass().getName()
                        : "null"));
        part.setModel(modelElement);
        return part;
    }

}