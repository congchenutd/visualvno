package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.ui.VNOEditor;

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
        VNOEditor editor = getActiveEditor();
        if(!editor.getTitle().startsWith("Global"))
        {
            DiagramModel globalDiagram = getEditor("Global.vno").getModel();
            DiagramModel diagram = editor.getModel();
            globalDiagram.removeNetwork(diagram.getVNOID());
        }
    }
}