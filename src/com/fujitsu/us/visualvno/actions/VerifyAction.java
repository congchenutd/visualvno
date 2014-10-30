package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.demo.Demo;
import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.ui.VNOEditor;

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
        VNOEditor editor = getActiveEditor();
        String title = editor.getTitle();
        if(title.startsWith("VNO"))
        {
            int vnoID = Integer.valueOf("" + title.charAt(3));
            if(!Demo.getInstance().canVerify(vnoID))
            	return;
            
            String wholeEditorName  = "VNO" + vnoID + ".vno";
            String wholeDiagramName = "VNO" + vnoID + "Whole.vno";
            DiagramModel diagram = Demo.getInstance().loadDiagram(wholeDiagramName);
            getEditor(wholeEditorName).setDiagram(diagram);
            
            Demo.getInstance().verify(vnoID);
        }
    }
}
