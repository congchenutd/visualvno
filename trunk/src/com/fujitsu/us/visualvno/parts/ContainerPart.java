package com.fujitsu.us.visualvno.parts;

import java.util.List;

import org.eclipse.gef.EditPolicy;

import com.fujitsu.us.visualvno.model.ContainerModel;
import com.fujitsu.us.visualvno.model.ShapeModel;
import com.fujitsu.us.visualvno.parts.policies.ContainerPolicy;

public abstract class ContainerPart extends ShapePart
{
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new ContainerPolicy());
    }
    
    @Override
    protected List<ShapeModel> getModelChildren() {
        return ((ContainerModel) getModel()).getChildren();
    }
}
