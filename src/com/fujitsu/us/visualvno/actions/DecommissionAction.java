package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.ui.VNOEditor;

public class DecommissionAction extends ActionBase
{
    public static final String ID = "DECOMMISSION";
    
    public DecommissionAction()
    {
        setId(ID);
        setText("Decommission");
        this.setImageDescriptor(ImageDescriptor.createFromFile(VisualVNOPlugin.class,
                                                               "icons/Delete.png"));
    }
    
    @Override
    public void run()
    {
        VNOEditor editor = getActiveEditor();
        if(!editor.getTitle().startsWith("Global"))
        {
            DiagramModel globalDiagram = getEditor("Global.vno").getModel();
            DiagramModel diagram = editor.getModel();
            diagram.removeNetwork(diagram.getVNOID());
            diagram.removeNetwork(0);
            globalDiagram.removeNetwork(diagram.getVNOID());
        }
    }
}
