package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.Demo;
import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.ui.VNOEditor;

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
        VNOEditor editor = getActiveEditor();
        if(!editor.getTitle().startsWith("Global"))
        {
            DiagramModel diagram = editor.getModel();
            diagram.addNetwork(Demo.getInstance().createVirtualNetwork(diagram.getVNOID()));
        }
    }
}
