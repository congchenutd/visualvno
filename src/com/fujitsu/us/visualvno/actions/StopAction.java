package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.demo.Demo;
import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.model.Network;

public class StopAction extends ActionBase
{
    public static final String ID = "STOP";
    
    public StopAction()
    {
        setId(ID);
        setText("Stop");
        setImageDescriptor(ImageDescriptor.createFromFile(VisualVNOPlugin.class,
                                                          "icons/Stop.png"));
    }
    
    @Override
    public void run()
    {
        String title = getActiveEditor().getTitle();
        if(title.startsWith("VNO"))
        {
            int vnoID = Integer.valueOf("" + title.charAt(3));
            if(!Demo.getInstance().canStop(vnoID))
            	return;
            
            // remove the vno from the global diagram
            DiagramModel globalDiagram = getGlobalEditor().getDiagram();
            globalDiagram.removeNetwork(vnoID);
            
            // de-highlight the network
            Network userNetwork     = new Network(getUserEditor    (vnoID).getDiagram(), vnoID);
            Network internalNetwork = new Network(getInternalEditor(vnoID).getDiagram(), vnoID);
            userNetwork    .deHighlightLinks();
            internalNetwork.deHighlightLinks();
            Demo.getInstance().getNetwork(vnoID).deHighlightLinks();
            
            Demo.getInstance().stop(vnoID);
        }
    }
}