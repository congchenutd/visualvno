package com.fujitsu.us.visualvno.ui;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.AlignmentRetargetAction;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.MatchSizeRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;

import com.fujitsu.us.visualvno.actions.PauseAction;
import com.fujitsu.us.visualvno.actions.StartAction;
import com.fujitsu.us.visualvno.actions.StopAction;

/**
 * Contributes actions to a toolbar. 
 * This class is tied to the editor in plugin.xml
 */
public class EditorActionBarContributor extends ActionBarContributor
{

	/**
	 * Create actions managed by this contributor.
	 */
	@SuppressWarnings("restriction")
    @Override
    protected void buildActions()
	{
		addRetargetAction(new DeleteRetargetAction());
		addRetargetAction(new UndoRetargetAction());
		addRetargetAction(new RedoRetargetAction());
		
		addRetargetAction(new ZoomInRetargetAction());
        addRetargetAction(new ZoomOutRetargetAction());
       
        addRetargetAction(new RetargetAction(
            GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY,
            GEFMessages.ToggleSnapToGeometry_Label, IAction.AS_CHECK_BOX));

        addRetargetAction(new RetargetAction(
            GEFActionConstants.TOGGLE_GRID_VISIBILITY,
            GEFMessages.ToggleGrid_Label, IAction.AS_CHECK_BOX));
        
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.LEFT));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.CENTER));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.RIGHT));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.TOP));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.MIDDLE));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.BOTTOM));
        
        addRetargetAction(new MatchSizeRetargetAction());
        
        addAction(new StartAction());
        addAction(new PauseAction());
        addAction(new StopAction());
	}

	/**
	 * Add actions to the given toolbar.
	 */
	@Override
    public void contributeToToolBar(IToolBarManager toolBarManager)
	{
		toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
		toolBarManager.add(getAction(ActionFactory.REDO.getId()));
		
		toolBarManager.add(new Separator());
		toolBarManager.add(getAction(GEFActionConstants.ZOOM_IN));
        toolBarManager.add(getAction(GEFActionConstants.ZOOM_OUT));
        
        toolBarManager.add(new Separator());
        toolBarManager.add(getAction(GEFActionConstants.ALIGN_LEFT));
        toolBarManager.add(getAction(GEFActionConstants.ALIGN_CENTER));
        toolBarManager.add(getAction(GEFActionConstants.ALIGN_RIGHT));
        toolBarManager.add(new Separator());
        toolBarManager.add(getAction(GEFActionConstants.ALIGN_TOP));
        toolBarManager.add(getAction(GEFActionConstants.ALIGN_MIDDLE));
        toolBarManager.add(getAction(GEFActionConstants.ALIGN_BOTTOM));
        
        toolBarManager.add(new Separator());
        toolBarManager.add(getAction(GEFActionConstants.MATCH_SIZE));
        
        toolBarManager.add(new Separator());
        String[] zoomStrings = new String[] {
                ZoomManager.FIT_ALL,
                ZoomManager.FIT_HEIGHT, 
                ZoomManager.FIT_WIDTH };
        toolBarManager.add(new ZoomComboContributionItem(getPage(), zoomStrings));
        
        toolBarManager.add(new Separator());
        toolBarManager.add(getAction(StartAction.ID));
        toolBarManager.add(getAction(PauseAction.ID));
        toolBarManager.add(getAction(StopAction.ID));
	}
	
	/**
	 * Add actions to the menu bar
	 */
	@Override
    public void contributeToMenu(IMenuManager menubar)
	{
        super.contributeToMenu(menubar);
        MenuManager viewMenu = new MenuManager("View");
        viewMenu.add(getAction(GEFActionConstants.ZOOM_IN));
        viewMenu.add(getAction(GEFActionConstants.ZOOM_OUT));
        
        viewMenu.add(new Separator());
        viewMenu.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
        viewMenu.add(getAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY));
        
        viewMenu.add(new Separator());
        viewMenu.add(getAction(GEFActionConstants.ALIGN_LEFT));
        viewMenu.add(getAction(GEFActionConstants.ALIGN_CENTER));
        viewMenu.add(getAction(GEFActionConstants.ALIGN_RIGHT));
        viewMenu.add(new Separator());
        viewMenu.add(getAction(GEFActionConstants.ALIGN_TOP));
        viewMenu.add(getAction(GEFActionConstants.ALIGN_MIDDLE));
        viewMenu.add(getAction(GEFActionConstants.ALIGN_BOTTOM));
        
        viewMenu.add(new Separator());
        viewMenu.add(getAction(GEFActionConstants.MATCH_SIZE));
        
        menubar.insertAfter(IWorkbenchActionConstants.M_EDIT, viewMenu);
    }

	/**
	 * Shortcut keys
	 */
	@Override
    protected void declareGlobalActionKeys() {
	    this.addGlobalActionKey(ActionFactory.SELECT_ALL.getId());
	}

}