package com.fujitsu.us.visualvno.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;

/**
 * Provides context menu actions for VNOEditor.
 */
public class VNOEditorContextMenuProvider extends ContextMenuProvider
{
	private final ActionRegistry actionRegistry;

	/**
	 * @param viewer	the editor's graphical viewer
	 * @param registry	the editor's action registry
	 */
	public VNOEditorContextMenuProvider(EditPartViewer viewer, 
										   ActionRegistry registry)
	{
		super(viewer);
		actionRegistry = registry;
	}

	/**
	 * Called when the context menu is about to show. 
	 */
	@Override
	public void buildContextMenu(IMenuManager menu)
	{
		// Add standard action groups to the menu
		GEFActionConstants.addStandardActionGroups(menu);
		
		menu.add(new Separator(GEFActionConstants.GROUP_UNDO));
		menu.add(new Separator(GEFActionConstants.GROUP_EDIT));

		// Add actions to the menu
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO,          // target group id
						   getAction(ActionFactory.UNDO.getId())); // action to add
		menu.appendToGroup(GEFActionConstants.GROUP_UNDO, 
		                   getAction(ActionFactory.REDO.getId()));
		menu.appendToGroup(GEFActionConstants.GROUP_EDIT, 
		                   getAction(ActionFactory.DELETE.getId()));
	}

	private IAction getAction(String actionId) {
		return actionRegistry.getAction(actionId);
	}

}
