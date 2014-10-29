package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.model.DiagramModel;

public class StopAction extends ActionBase
{
    public static final String ID = "STOP";
    
    public StopAction()
    {
        setId(ID);
        setText("Stop");
        this.setImageDescriptor(ImageDescriptor.createFromFile(VisualVNOPlugin.class,
                                                               "icons/Stop.png"));
    }
    
    @Override
    public void run()
    {
        String title = getActiveEditor().getTitle();
        if(title.startsWith("VNO"))
        {
            int vnoID = Integer.valueOf("" + title.charAt(3));
            DiagramModel globalDiagram = getEditor("Global.vno").getDiagram();
            globalDiagram.removeNetwork(vnoID);
        }
    }
}