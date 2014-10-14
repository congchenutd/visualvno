package com.fujitsu.us.visualvno.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;

import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.model.ModelBase;
import com.fujitsu.us.visualvno.model.ShapeBase;

/**
 * Tree edit part for the diagram model 
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class DiagramTreeEditPart extends    AbstractTreeEditPart
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
    protected void createEditPolicies()
    {
        if(getParent() instanceof RootEditPart)
            installEditPolicy(EditPolicy.COMPONENT_ROLE,
                    new RootComponentEditPolicy());
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
    protected List<ShapeBase> getModelChildren() {
        return getCastedModel().getChildren(); // a list of shapes
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        String prop = evt.getPropertyName();
        if(DiagramModel.CHILD_ADDED_PROP  .equals(prop) ||
           DiagramModel.CHILD_REMOVED_PROP.equals(prop))
            refreshChildren();
        else
            refreshVisuals();
    }

}
