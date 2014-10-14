package org.eclipse.gef.examples.shapes.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.gef.examples.shapes.model.ModelBase;
import org.eclipse.gef.examples.shapes.model.ShapeBase;
import org.eclipse.gef.examples.shapes.parts.policies.ShapeComponentPolicy;
import org.eclipse.swt.graphics.Image;

public class ShapeTreeEditPart extends AbstractTreeEditPart implements
        PropertyChangeListener
{

    public ShapeTreeEditPart(ShapeBase model)
    {
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
        // allow removal of the associated model element
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new ShapeComponentPolicy());
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

    private ShapeBase getCastedModel() {
        return (ShapeBase) getModel();
    }

    @Override
    protected Image getImage() {
        return getCastedModel().getIcon();
    }

    @Override
    protected String getText() {
        return getCastedModel().toString();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshVisuals();
    }
    
}