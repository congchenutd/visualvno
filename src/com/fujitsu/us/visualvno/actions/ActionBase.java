package com.fujitsu.us.visualvno.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;

import com.fujitsu.us.visualvno.ui.VNOEditor;

public abstract class ActionBase extends Action
{
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
