package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;

public class PauseAction extends Action
{
    public static final String ID = "PAUSE";
    
    public PauseAction()
    {
        setId(ID);
        setText("Pause");
        this.setImageDescriptor(ImageDescriptor.createFromFile(VisualVNOPlugin.class,
                                                               "icons/Pause.png"));
    }
    
    @Override
    public void run()
    {
        MessageDialog.openInformation(null, "Pause", "The VNO is paused");
    }
}