package com.fujitsu.us.visualvno.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;

import com.fujitsu.us.visualvno.model.DiagramModel;

/**
 * A WizardPage for creating an empty .vno file
 */
class NewVNODiagramPage extends WizardNewFileCreationPage
{
	private static final String	DEFAULT_EXTENSION = ".vno";
	private final IWorkbench	_workbench;

	NewVNODiagramPage(IWorkbench workbench, IStructuredSelection selection)
	{
		super("VNOCreationPage", selection);
		_workbench = workbench;
		setTitle("Create a new " + DEFAULT_EXTENSION + " file");
	}

	@Override
	public void createControl(Composite parent)
	{
		super.createControl(parent);
		setFileName("VNO" + NewVNODiagramWizard.getFileCount() + DEFAULT_EXTENSION);
		setPageComplete(validatePage());
	}

	/**
	 * This method will be invoked, when the "Finish" button is pressed.
	 */
	boolean finish()
	{
		// create a new file
		IFile newFile = createNewFile();
		NewVNODiagramWizard.setFileCount(NewVNODiagramWizard.getFileCount());

		// open newly created file in the editor
		IWorkbenchPage page = _workbench.getActiveWorkbenchWindow().getActivePage();
		if(newFile != null && page != null)
		{
			try	{
				IDE.openEditor(page, newFile, true);
			}
			catch(PartInitException e)
			{
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	@Override
	protected InputStream getInitialContents()
	{
		ByteArrayInputStream is = null;
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(new DiagramModel());  // must be Serializable
			oos.flush();
			oos.close();
			is = new ByteArrayInputStream(baos.toByteArray());
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		return is;
	}

	private boolean validateFilename()
	{
		if(getFileName() != null && getFileName().endsWith(DEFAULT_EXTENSION))
			return true;
		setErrorMessage("The 'file' name must end with " + DEFAULT_EXTENSION);
		return false;
	}

	@Override
	protected boolean validatePage() {
		return super.validatePage() && validateFilename();
	}
}