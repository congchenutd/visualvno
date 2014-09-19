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
import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.model.DiagramModel;

/**
 * TreeEditPart for a ShapesDiagram instance. 
 * Used in the Outline View of the ShapesEditor.
 */
public class DiagramTreeEditPart extends AbstractTreeEditPart 
                                 implements PropertyChangeListener
{

	public DiagramTreeEditPart(DiagramModel model) {
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
    
    private DiagramModel getCastedModel() {
        return (DiagramModel) getModel();
    }

    @Override
    protected void createEditPolicies()
    {
        // If this editpart is the root of the viewer, disallow removal
        if(getParent() instanceof RootEditPart)
            installEditPolicy(EditPolicy.COMPONENT_ROLE, 
                              new RootComponentEditPolicy());
    }

    /**
     * Convenience method that returns the EditPart for to a given child.
     */
    private EditPart getEditPartForChild(Object child) {
        return (EditPart) getViewer().getEditPartRegistry().get(child);
    }

    @Override
    protected List<ShapeModel> getModelChildren() {
        return getCastedModel().getChildren();
    }

    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        String property = event.getPropertyName();
        if(DiagramModel.CHILD_ADDED_PROP.equals(property))
            addChild(createChild(event.getNewValue()), -1);
        else if(DiagramModel.CHILD_REMOVED_PROP.equals(property))
            removeChild(getEditPartForChild(event.getNewValue()));
        else
            refreshVisuals();
    }
}
