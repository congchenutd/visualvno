package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;

public class VerifyAction extends Action
{
    public static final String ID = "VERIFY";
    
    public VerifyAction()
    {
        setId(ID);
        setText("Verify");
        this.setImageDescriptor(ImageDescriptor.createFromFile(VisualVNOPlugin.class,
                                                               "icons/Verify.png"));
    }
    
    @Override
    public void run()
    {
        MessageDialog.openInformation(null, "Verify", "The VNO is verified");
    }
}
