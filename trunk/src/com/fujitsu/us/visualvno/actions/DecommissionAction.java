package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;

public class DecommissionAction extends Action
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
        MessageDialog.openInformation(null, "Decommission", "The VNO is decommissioned");
    }
}
