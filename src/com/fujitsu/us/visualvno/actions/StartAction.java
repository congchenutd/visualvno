package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.ui.VNOEditor;

public class StartAction extends Action
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
//        MessageDialog.openInformation(null, "Start", "The VNO is started");
        
        VNOEditor editor = (VNOEditor) PlatformUI.getWorkbench()
                                                 .getActiveWorkbenchWindow()
                                                 .getActivePage()
                                                 .getActiveEditor();
        editor.doSetInput();
    }
}
