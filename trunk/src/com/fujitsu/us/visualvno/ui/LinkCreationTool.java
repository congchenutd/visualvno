package com.fujitsu.us.visualvno.ui;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.ConnectionCreationTool;
import org.eclipse.swt.widgets.Display;

import com.fujitsu.us.visualvno.model.LinkModel;
import com.fujitsu.us.visualvno.model.commands.LinkCreateCommand;

/**
 * Run direct edit after creation 
 * @author Cong
 */
public class LinkCreationTool extends ConnectionCreationTool
{
	@Override
	protected boolean handleCreateConnection()
	{
		boolean result = super.handleCreateConnection();

		// find the edit part of the link
		LinkModel model = ((LinkCreateCommand) getCommand()).getLink();
		final Object obj = getCurrentViewer().getEditPartRegistry().get(model);
		if(obj instanceof EditPart)
		{
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
		return result;
	}
}
