package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.demo.Demo;
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
        String title = editor.getTitle();
        if(title.startsWith("VNO"))
        {
            int vnoID = Integer.valueOf("" + title.charAt(3));
            String virtualEditorName  = "VNO" + vnoID + "Virtual.vno";
            String wholeEditorName    = "VNO" + vnoID + ".vno";
            getEditor(virtualEditorName).setDiagram(new DiagramModel());
            getEditor(wholeEditorName)  .setDiagram(new DiagramModel());
            
            DiagramModel globalDiagram = getEditor("Global.vno").getDiagram();
            globalDiagram.removeNetwork(vnoID);
            
            Demo.getInstance().decommission(vnoID);
        }
    }
}
