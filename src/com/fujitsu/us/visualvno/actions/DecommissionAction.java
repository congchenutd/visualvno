package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.demo.Demo;
import com.fujitsu.us.visualvno.model.DiagramModel;

public class DecommissionAction extends ActionBase
{
    public static final String ID = "DECOMMISSION";
    
    public DecommissionAction()
    {
        setId(ID);
        setText("Decommission");
        this.setImageDescriptor(ImageDescriptor.createFromFile(VisualVNOPlugin.class,
                                                               "icons/Delete.png"));
    }
    
    @Override
    public void run()
    {
        String title = getActiveEditor().getTitle();
        if(title.startsWith("VNO"))
        {
            int vnoID = Integer.valueOf("" + title.charAt(3));
            if(!Demo.getInstance().canDecommission(vnoID))
            	return;
            
            // set user and internal editor empty
            getUserEditor    (vnoID).setDiagram(new DiagramModel());
            getInternalEditor(vnoID).setDiagram(new DiagramModel());
            
            // remove the vno from the global diagram
            DiagramModel globalDiagram = getGlobalEditor().getDiagram();
            globalDiagram.removeNetwork(vnoID);
            
            Demo.getInstance().decommission(vnoID);
        }
    }
}
