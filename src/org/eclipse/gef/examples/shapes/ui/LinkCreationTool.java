package org.eclipse.gef.examples.shapes.ui;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.shapes.model.LinkBase;
import org.eclipse.gef.examples.shapes.model.commands.LinkCreateCommand;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.ConnectionCreationTool;
import org.eclipse.swt.widgets.Display;

/**
 * Run direct edit after creation
 * Works with CreationEntry, see EditorPaletteFactory
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class LinkCreationTool extends ConnectionCreationTool
{
	@Override
	protected boolean handleCreateConnection()
	{
		boolean result = super.handleCreateConnection();

		// find the edit part of the link
		Command cmd = getCommand();
		if(cmd == null)      // it's possible that the operation is interrupted
		    return result;
		LinkBase model = ((LinkCreateCommand) cmd).getLink();
		
		Object obj = getCurrentViewer().getEditPartRegistry().get(model);
		if(obj instanceof EditPart)
		{
			Display.getCurrent().asyncExec(new Runnable()
			{
				@Override
				public void run()
				{
					EditPart part = (EditPart) obj;
					part.performRequest(new DirectEditRequest());
				}
			});
		}
		return result;
	}
}
