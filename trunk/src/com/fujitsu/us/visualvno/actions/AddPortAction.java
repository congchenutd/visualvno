package com.fujitsu.us.visualvno.actions;

import java.util.List;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.parts.ShapeEditPart;

public class AddPortAction extends SelectionAction
{
    public  static final String ID      = "AddPortAction";
    private static final String REQUEST = "AddPortAction";
    private final Request request = new Request(REQUEST);
    
    public AddPortAction(IWorkbenchPart part)
    {
        super(part);
        setText("Add a port");
        setId(ID);
        setImageDescriptor(ImageDescriptor.createFromFile(VisualVNOPlugin.class,
                                                          "icons/Port.png"));
    }

    @Override
    public void run()
    {
        List<ShapeEditPart> editParts = getSelectedObjects();
        CompoundCommand compoundCommand = new CompoundCommand();
        compoundCommand.add(editParts.get(0).getCommand(request));
        execute(compoundCommand);
    }
    
    @Override
    protected boolean calculateEnabled()
    {
        if(getSelectedObjects().isEmpty())
            return false;

        for(Object selectedObject : getSelectedObjects())
            if(!(selectedObject instanceof ShapeEditPart))
                return false;
        return true;
    }
}
