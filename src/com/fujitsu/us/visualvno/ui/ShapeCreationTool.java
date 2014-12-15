package com.fujitsu.us.visualvno.ui;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.swt.widgets.Display;

/**
 * Run direct edit after creation
 * Works with CreationEntry, see EditorPaletteFactory
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class ShapeCreationTool extends CreationTool
{
	@Override
	protected void performCreation(int button)
	{
		super.performCreation(button);

		EditPartViewer viewer = getCurrentViewer();
		final Object model = getCreateRequest().getNewObject();
		if(model == null || viewer == null)
			return;

		// find the edit part of the shape
		final Object obj = viewer.getEditPartRegistry().get(model);
		if(obj instanceof EditPart)
		{
		    // send a direct edit request to the edit part
			Display.getCurrent().asyncExec(new Runnable()
			{
				@Override
				public void run()
				{
					EditPart part = (EditPart) obj;
					Request request = new DirectEditRequest();
					part.performRequest(request);
				}
			});
		}
	}
}