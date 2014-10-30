package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.demo.Demo;
import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.ui.VNOEditor;

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
        VNOEditor editor = getActiveEditor();
        String title = editor.getTitle();
        if(title.startsWith("VNO"))
        {
            int vnoID = Integer.valueOf("" + title.charAt(3));
            if(!Demo.getInstance().canStart(vnoID))
            	return;
            
            DiagramModel globalDiagram = getEditor("Global.vno").getDiagram();
            if(vnoID == 1)
                globalDiagram.addNetwork(Demo.getInstance()._network1);
            else
                globalDiagram.addNetwork(Demo.getInstance()._network2);
            
            Demo.getInstance().start(vnoID);
        }
    }
}
