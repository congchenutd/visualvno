package com.fujitsu.us.visualvno.ui;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;

/**
 * Contributes actions to a toolbars 
 * Tied to the editor in plugin.xml
 */
public class VNOEditorActionBarContributor extends ActionBarContributor
{

	/**
	 * Create actions managed by this contributor.
	 */
	@Override
	protected void buildActions()
	{
		addRetargetAction(new DeleteRetargetAction());
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		addRetargetAction(new ZoomInRetargetAction());
        addRetargetAction(new ZoomOutRetargetAction());
	}

	/**
	 * Add actions to the given toolbar.
	 */
	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager)
	{
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
		
        toolBarManager.add(getAction(GEFActionConstants.ZOOM_IN));
        toolBarManager.add(getAction(GEFActionConstants.ZOOM_OUT));
	}

	@Override
	protected void declareGlobalActionKeys() {
		this.addGlobalActionKey(ActionFactory.SELECT_ALL.getId());
	}

}
