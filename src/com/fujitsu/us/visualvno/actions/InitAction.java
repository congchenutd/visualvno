package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.demo.Demo;
import com.fujitsu.us.visualvno.model.DiagramModel;

public class InitAction extends ActionBase
{
    public static final String ID = "INIT";
    private boolean first = true;
    
    public InitAction()
    {
        setId(ID);
        setText("Initialize");
        setImageDescriptor(ImageDescriptor.createFromFile(VisualVNOPlugin.class,
                                                          "icons/Init.png"));
    }
    
    @Override
    public void run()
    {
        String title = getActiveEditor().getTitle();
        if(title.startsWith("VNO"))
        {
            int vnoID = Integer.valueOf("" + title.charAt(3));
            if(!Demo.getInstance().canInit(vnoID))
            	return;
            
            // load the virtual diagram to both user and internal editors
            DiagramModel diagram = loadVirtualDiagram(vnoID);
            getUserEditor    (vnoID).setDiagram(diagram);
            getInternalEditor(vnoID).setDiagram(diagram);
            
            // load global (physical) diagram
            if(first)
            {
                Demo.getInstance().loadGlobal(getGlobalEditor());
                first = false;
            }
            Demo.getInstance().init(vnoID);
        }
    }
}
