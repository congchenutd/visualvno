package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.Demo;
import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.model.DiagramModel;

public class VerifyAction extends ActionBase
{
    public static final String ID = "VERIFY";
    
    public VerifyAction()
    {
        setId(ID);
        setText("Verify");
        this.setImageDescriptor(ImageDescriptor.createFromFile(VisualVNOPlugin.class,
                                                               "icons/Verify.png"));
    }
    
    @Override
    public void run()
    {
        String title = getActiveEditor().getTitle();
        if(title.startsWith("VNO"))
        {
            int vnoID = Integer.valueOf("" + title.charAt(3));
            String wholeEditorName  = "VNO" + vnoID + ".vno";
            String wholeDiagramName = "VNO" + vnoID + "Whole.vno";
            DiagramModel diagram = Demo.getInstance().loadDiagram(wholeDiagramName);
            getEditor(wholeEditorName).setDiagram(diagram);
        }
    }
}
