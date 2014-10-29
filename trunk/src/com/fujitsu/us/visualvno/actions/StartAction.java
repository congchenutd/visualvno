package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.Demo;
import com.fujitsu.us.visualvno.VisualVNOPlugin;
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
        if(!editor.getTitle().startsWith("Global"))
        {
            DiagramModel globalDiagram = getEditor("Global.vno").getModel();
            DiagramModel diagram = editor.getModel();
            globalDiagram.addNetwork(Demo.getInstance().createVirtualNetwork(diagram.getVNOID()));
        }
    }
}
