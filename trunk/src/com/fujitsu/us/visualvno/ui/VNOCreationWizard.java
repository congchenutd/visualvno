package com.fujitsu.us.visualvno.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;

import com.fujitsu.us.visualvno.model.DiagramModel;

public class VNOCreationWizard extends Wizard implements INewWizard
{

    private static int   _fileCount = 1;
    private CreationPage _page1;

    @Override
    public void addPages() {
        addPage(_page1);
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        _page1 = new CreationPage(workbench, selection);
    }

    @Override
    public boolean performFinish() {
        return _page1.finish();
    }

    /**
     * A wizard page that creates an empty .vno file for the VNOEditor.
     */
    private class CreationPage extends WizardNewFileCreationPage
    {
        private static final String DEFAULT_EXTENSION = ".vno";
        private final IWorkbench    workbench;

        CreationPage(IWorkbench workbench, IStructuredSelection selection)
        {
            super("VNOCreationPage1", selection);
            this.workbench = workbench;
            setTitle        ("Create a new " + DEFAULT_EXTENSION + " file");
            setDescription  ("Create a new " + DEFAULT_EXTENSION + " file");
        }

        @Override
        public void createControl(Composite parent)
        {
            super.createControl(parent);
            setFileName("VNO" + _fileCount + DEFAULT_EXTENSION);
            setPageComplete(validatePage());
        }

        private Object createDefaultContent() {
            return new DiagramModel();
        }

        /**
         * This method will be invoked when the "Finish" button is pressed.
         */
        boolean finish()
        {
            // create a new file, result != null if successful
            IFile newFile = createNewFile();
            _fileCount++;

            // open newly created file in the editor
            IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
            if(newFile != null && page != null)
            {
                try {
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
            ByteArrayInputStream bais = null;
            try
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(createDefaultContent()); // argument must be Serializable
                oos.flush();
                oos.close();
                bais = new ByteArrayInputStream(baos.toByteArray());
            }
            catch(IOException ioe) {
                ioe.printStackTrace();
            }
            return bais;
        }

        /**
         * Return true, if the file name entered in this page is valid.
         */
        private boolean validateFilename()
        {
            if(getFileName() != null && 
               getFileName().endsWith(DEFAULT_EXTENSION))
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
