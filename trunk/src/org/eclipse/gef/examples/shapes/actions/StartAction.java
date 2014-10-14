package org.eclipse.gef.examples.shapes.actions;

import org.eclipse.gef.examples.shapes.VisualVNOPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;

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
        MessageDialog.openInformation(null, "Start", "The VNO is started");
    }
}
