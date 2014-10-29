package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.demo.Demo;
import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.ui.VNOEditor;

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
        VNOEditor editor = getActiveEditor();
        String title = getActiveEditor().getTitle();
        if(title.startsWith("VNO"))
        {
            int vnoID = Integer.valueOf("" + title.charAt(3));
            String virtualEditorName  = "VNO" + vnoID + "Virtual.vno";
            String wholeEditorName    = "VNO" + vnoID + ".vno";
            String virtualDiagramName = virtualEditorName;
            DiagramModel diagram = Demo.getInstance().loadDiagram(virtualDiagramName);
            getEditor(virtualEditorName).setDiagram(diagram);
            getEditor(wholeEditorName)  .setDiagram(diagram);
            
            if(first)
            {
                Demo.getInstance().loadGlobal(getEditor("Global.vno"));
                first = false;
            }
            Demo.getInstance().init();
        }
    }
}
