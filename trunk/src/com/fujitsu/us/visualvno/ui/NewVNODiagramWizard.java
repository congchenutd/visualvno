package com.fujitsu.us.visualvno.ui;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Wizard for creating a new .vno file.
 * Those files can be used with the Editor (see plugin.xml).
 */
public class NewVNODiagramWizard extends Wizard implements INewWizard
{

	private static int         _fileCount = 1;
	private NewVNODiagramPage  _page;
	
	public static int getFileCount() {
	    return _fileCount;
	}
	
	public static void setFileCount(int count) {
	    if(count > 0)
	        _fileCount = count;
	}

	@Override
	public void addPages() {
		addPage(_page);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		_page = new NewVNODiagramPage(workbench, selection);
	}

	@Override
	public boolean performFinish() {
		return _page.finish();
	}
}
