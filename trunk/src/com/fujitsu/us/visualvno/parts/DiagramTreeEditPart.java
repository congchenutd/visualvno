package com.fujitsu.us.visualvno.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;

import com.fujitsu.us.visualvno.model.ModelBase;
import com.fujitsu.us.visualvno.model.Shape;
import com.fujitsu.us.visualvno.model.Diagram;

/**
 * TreeEditPart for a ShapesDiagram instance. 
 * Used in the Outline View of the ShapesEditor.
 */
class DiagramTreeEditPart extends AbstractTreeEditPart 
                          implements PropertyChangeListener
{

	DiagramTreeEditPart(Diagram model) {
		super(model);
	}

    @Override
    public void activate()
    {
        if(!isActive())
        {
            super.activate();
            ((ModelBase) getModel()).addPropertyChangeListener(this);
        }
    }
    
    @Override
    public void deactivate()
    {
        if(isActive())
        {
            super.deactivate();
            ((ModelBase) getModel()).removePropertyChangeListener(this);
        }
    }
    
    private Diagram getCastedModel() {
        return (Diagram) getModel();
    }

    @Override
    protected void createEditPolicies()
    {
        // If this editpart is the root of the viewer, disallow removal
        if(getParent() instanceof RootEditPart) {
            installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
        }
    }

    /**
     * Convenience method that returns the EditPart for to a given child.
     */
    private EditPart getEditPartForChild(Object child) {
        return (EditPart) getViewer().getEditPartRegistry().get(child);
    }

    @Override
    protected List<Shape> getModelChildren() {
        return getCastedModel().getChildren();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        String prop = evt.getPropertyName();
        if(Diagram.CHILD_ADDED_PROP.equals(prop))
            addChild(createChild(evt.getNewValue()), -1);
        else if(Diagram.CHILD_REMOVED_PROP.equals(prop))
            removeChild(getEditPartForChild(evt.getNewValue()));
        else
            refreshVisuals();
    }
}
