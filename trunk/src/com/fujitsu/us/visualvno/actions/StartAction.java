package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.demo.Demo;
import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.model.Network;

public class StartAction extends ActionBase
{
    public static final String ID = "START";
    
    public StartAction()
    {
        setId(ID);
        setText("Start");
        setImageDescriptor(ImageDescriptor.createFromFile(VisualVNOPlugin.class,
                                                          "icons/Start.png"));
    }
    
    @Override
    public void run()
    {
        String title = getActiveEditor().getTitle();
        if(title.startsWith("VNO"))
        {
            int vnoID = Integer.valueOf("" + title.charAt(3));
            if(!Demo.getInstance().canStart(vnoID))
            	return;
            
            // add the network to the global diagram
            DiagramModel globalDiagram = getGlobalEditor().getDiagram();
            globalDiagram.addNetwork(Demo.getInstance().getNetwork(vnoID));
            
            // highlight the links
            Network userNetwork     = new Network(getUserEditor    (vnoID).getDiagram(), vnoID);
            Network internalNetwork = new Network(getInternalEditor(vnoID).getDiagram(), vnoID);
            userNetwork    .highlightLinks();
            internalNetwork.highlightLinks();
            Demo.getInstance().getNetwork(vnoID).highlightLinks();
            
            Demo.getInstance().start(vnoID);
        }
    }
}
