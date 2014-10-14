package com.fujitsu.us.visualvno.ui;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Provides context menu actions for VNOEditor.
 */
class EditorContextMenuProvider extends ContextMenuProvider
{

	private ActionRegistry actionRegistry;

	public EditorContextMenuProvider(EditPartViewer viewer, ActionRegistry registry)
	{
		super(viewer);
		if(registry == null)
			throw new IllegalArgumentException();
		actionRegistry = registry;
	}

	/**
	 * Called when the context menu is about to show. 
	 * Actions, whose state is enabled, will appear in the context menu.
	 */
	@Override
    public void buildContextMenu(IMenuManager menu)
	{
		// Add standard action groups to the menu
		GEFActionConstants.addStandardActionGroups(menu);

		// Add actions to the menu
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO,           // group id
				           getAction(ActionFactory.UNDO.getId()));  // action id
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO,
				           getAction(ActionFactory.REDO.getId()));
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT,
				           getAction(ActionFactory.DELETE.getId()));
	}

	private IAction getAction(String actionId) {
		return actionRegistry.getAction(actionId);
	}

}
