package com.fujitsu.us.visualvno.parts.factories;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.model.HostModel;
import com.fujitsu.us.visualvno.model.LinkModel;
import com.fujitsu.us.visualvno.model.PortModel;
import com.fujitsu.us.visualvno.model.SwitchModel;
import com.fujitsu.us.visualvno.parts.DiagramPart;
import com.fujitsu.us.visualvno.parts.HostPart;
import com.fujitsu.us.visualvno.parts.LinkPart;
import com.fujitsu.us.visualvno.parts.PortPart;
import com.fujitsu.us.visualvno.parts.SwitchPart;

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
        if(modelElement instanceof PortModel)
            return new PortPart();
        if(modelElement instanceof DiagramModel)
            return new DiagramPart();
        if(modelElement instanceof SwitchModel)
            return new SwitchPart();
        if(modelElement instanceof HostModel)
            return new HostPart();
        if(modelElement instanceof LinkModel)
            return new LinkPart();
        throw new RuntimeException(
               "Can't create part for model element: " + 
               ((modelElement != null) ? modelElement.getClass().getName() 
                                       : "null"));
    }

}
