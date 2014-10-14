package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;

public class StopAction extends Action
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
        MessageDialog.openInformation(null, "Stop", "The VNO is stopped");
    }
}