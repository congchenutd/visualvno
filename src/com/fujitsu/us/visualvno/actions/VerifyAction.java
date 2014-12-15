package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.demo.Demo;
import com.fujitsu.us.visualvno.model.DiagramModel;

public class VerifyAction extends ActionBase
{
    public static final String ID = "VERIFY";
    
    public VerifyAction()
    {
        setId(ID);
        setText("Verify");
        setImageDescriptor(ImageDescriptor.createFromFile(VisualVNOPlugin.class,
                                                          "icons/Verify.png"));
    }
    
    @Override
    public void run()
    {
        String title = getActiveEditor().getTitle();
        if(title.startsWith("VNO"))
        {
            int vnoID = Integer.valueOf("" + title.charAt(3));
            if(!Demo.getInstance().canVerify(vnoID))
            	return;
            
            // load the whole diagram of the vno
            DiagramModel diagram = loadWholeDiagram(vnoID);
            getInternalEditor(vnoID).setDiagram(diagram);
            
            Demo.getInstance().verify(vnoID);
        }
    }
}
