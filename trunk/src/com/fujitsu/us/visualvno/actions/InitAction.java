package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.Demo;
import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.ui.VNOEditor;

public class InitAction extends ActionBase
{
    public static final String ID = "INIT";
    
    public InitAction()
    {
        setId(ID);
        setText("Initialize");
        this.setImageDescriptor(ImageDescriptor.createFromFile(VisualVNOPlugin.class,
                                                               "icons/Init.png"));
    }
    
    @Override
    public void run()
    {
        VNOEditor editor = getActiveEditor();
        
        if(!editor.getTitle().startsWith("Global"))
        {
            DiagramModel diagram = editor.getModel();
            diagram.addNetwork(Demo.getInstance().createPhysicalNetwork(diagram.getVNOID()));
        }
    }
}
