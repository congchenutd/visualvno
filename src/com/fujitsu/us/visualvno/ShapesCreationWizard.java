package com.fujitsu.us.visualvno;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;

import com.fujitsu.us.visualvno.model.Diagram;

/**
 * Create a new .vno file.
 * Those files can be used with the ShapesEditor (see plugin.xml).
 */
public class ShapesCreationWizard extends Wizard implements INewWizard
{

	private static int	 fileCount = 1;
	private CreationPage page;

	@Override
	public void addPages() {
		addPage(page);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		page = new CreationPage(workbench, selection);
	}

	@Override
	public boolean performFinish() {
		return page.finish();
	}

	/**
	 * This WizardPage can create an empty .vno file for the ShapesEditor.
	 */
	private class CreationPage extends WizardNewFileCreationPage
	{
		private static final String	DEFAULT_EXTENSION = ".vno";
		private final IWorkbench	workbench;

		/**
		 * Create a new wizard page instance.
		 * 
		 * @param workbench	the current workbench
		 * @param selection	the current object selection
		 */
		CreationPage(IWorkbench workbench, IStructuredSelection selection)
		{
			super("VNOCreationPage", selection);
			this.workbench = workbench;
			setTitle      ("Create a new " + DEFAULT_EXTENSION + " file");
			setDescription("Create a new " + DEFAULT_EXTENSION + " file");
		}

		@Override
		public void createControl(Composite parent)
		{
			super.createControl(parent);
			setFileName("VNO" + fileCount + DEFAULT_EXTENSION);
			setPageComplete(validatePage());
		}

		/** Return a new ShapesDiagram instance. */
		private Object createDefaultContent() {
			return new Diagram();
		}

		/**
		 * This method will be invoked, when the "Finish" button is pressed.
		 */
		boolean finish()
		{
			// create a new file, result != null if successful
			IFile newFile = createNewFile();
			fileCount++;

			// open newly created file in the editor
			IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
			if(newFile != null && page != null)
			{
				try
				{
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
				oos.writeObject(createDefaultContent()); // must be Serializable
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
}
