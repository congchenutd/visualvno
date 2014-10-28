package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;

public class InitAction extends Action
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
        MessageDialog.openInformation(null, "Initialize", "The VNO is initialized");
    }
}
