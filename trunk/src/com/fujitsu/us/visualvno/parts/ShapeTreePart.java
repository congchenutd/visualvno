package com.fujitsu.us.visualvno.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;

import com.fujitsu.us.visualvno.model.ModelBase;
import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.parts.policies.ShapeRemovalPolicy;

/**
 * TreeEditPart used for Shape instances.
 * This is used in the Outline View of the ShapesEditor.
 */
public class ShapeTreePart extends      AbstractTreeEditPart 
                           implements   PropertyChangeListener
{
    public ShapeTreePart(ShapeModel model) {
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
        installEditPolicy(EditPolicy.COMPONENT_ROLE, 
                          new ShapeRemovalPolicy());
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

    private ShapeModel getCastedModel() {
        return (ShapeModel) getModel();
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
        refreshVisuals(); // this will cause an invocation of getImage() and getText()
    }
}
