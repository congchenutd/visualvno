package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import com.fujitsu.us.visualvno.ui.VNOEditor;

public abstract class ActionBase extends Action
{
    public static InitAction            INIT            = new InitAction();
    public static VerifyAction          VERIFY          = new VerifyAction();
    public static StartAction           START           = new StartAction();
    public static StopAction            STOP            = new StopAction();
    public static DecommissionAction    DECOMMISSION    = new DecommissionAction();
    
    protected VNOEditor getActiveEditor() {
        return (VNOEditor) PlatformUI.getWorkbench()
                                     .getActiveWorkbenchWindow()
                                     .getActivePage()
                                     .getActiveEditor();
    }
    
    protected VNOEditor getEditor(String name)
    {
        IEditorReference[] references = PlatformUI.getWorkbench()
                                                  .getActiveWorkbenchWindow()
                                                  .getActivePage()
                                                  .getEditorReferences();
        for(IEditorReference ref: references)
            if(ref.getName().equals(name))
                return (VNOEditor) ref.getEditor(true);
        return null;
    }
}
