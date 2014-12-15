package com.fujitsu.us.visualvno.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.rulers.RulerProvider;

import com.fujitsu.us.visualvno.model.ContainerModel;
import com.fujitsu.us.visualvno.model.ShapeBase;
import com.fujitsu.us.visualvno.parts.policies.ContainerLayoutPolicy;
import com.fujitsu.us.visualvno.parts.policies.ContainerPolicy;

/**
 * Edit part for all the containers
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public abstract class ContainerEditPart extends ShapeEditPart
{
    @Override
    protected void createEditPolicies()
    {
        super.createEditPolicies();
        
        // generate an orphan command for dragging a child out of its parent
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new ContainerPolicy());
        
        // for creating, moving, adding children  
        installEditPolicy(EditPolicy.LAYOUT_ROLE,    new ContainerLayoutPolicy());
        
        // enable snap to geometry guides
        installEditPolicy("Snap Feedback", new SnapFeedbackPolicy());
    }
    
    @Override
    protected List<ShapeBase> getModelChildren() {
        return ((ContainerModel) getModel()).getChildren();
    }
    
    @Override
    @SuppressWarnings("rawtypes")
    public Object getAdapter(Class adapter)
    {
        if(adapter == SnapToHelper.class)
        {
            List<SnapToHelper> snapStrategies = new ArrayList<SnapToHelper>();
            Boolean val = (Boolean) getViewer().getProperty(
                    RulerProvider.PROPERTY_RULER_VISIBILITY);
            if(val != null && val.booleanValue())
                snapStrategies.add(new SnapToGuides(this));
            
            val = (Boolean) getViewer().getProperty(
                    SnapToGeometry.PROPERTY_SNAP_ENABLED);
            if(val != null && val.booleanValue())
                snapStrategies.add(new SnapToGeometry(this));
            
            val = (Boolean) getViewer().getProperty(
                    SnapToGrid.PROPERTY_GRID_ENABLED);
            if(val != null && val.booleanValue())
                snapStrategies.add(new SnapToGrid(this));

            if(snapStrategies.size() == 0)
                return null;
            if(snapStrategies.size() == 1)
                return snapStrategies.get(0);

            SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
            for(int i = 0; i < snapStrategies.size(); i++)
                ss[i] = snapStrategies.get(i);
            return new CompoundSnapToHelper(ss);
        }
        return super.getAdapter(adapter);
    }
}