package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.demo.Demo;
import com.fujitsu.us.visualvno.model.DiagramModel;

public class StartAction extends ActionBase
{
    public static final String ID = "START";
    
    public StartAction()
    {
        setId(ID);
        setText("Start");
        this.setImageDescriptor(ImageDescriptor.createFromFile(VisualVNOPlugin.class,
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
            
            DiagramModel globalDiagram = getGlobalEditor().getDiagram();
            globalDiagram.addNetwork(Demo.getInstance().getNetwork(vnoID));
            
//            getUserEditor    (vnoID).getDiagram()
//            getInternalEditor(vnoID).getDiagram();
            
            Demo.getInstance().start(vnoID);
        }
    }
}
