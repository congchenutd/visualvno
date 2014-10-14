package com.fujitsu.us.visualvno.parts.factories;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.model.HostModel;
import com.fujitsu.us.visualvno.model.LinkBase;
import com.fujitsu.us.visualvno.model.PortModel;
import com.fujitsu.us.visualvno.model.SwitchModel;
import com.fujitsu.us.visualvno.parts.DiagramEditPart;
import com.fujitsu.us.visualvno.parts.HostEditPart;
import com.fujitsu.us.visualvno.parts.LinkEditPart;
import com.fujitsu.us.visualvno.parts.PortEditPart;
import com.fujitsu.us.visualvno.parts.SwitchEditPart;

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