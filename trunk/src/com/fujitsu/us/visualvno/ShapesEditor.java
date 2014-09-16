package com.fujitsu.us.visualvno;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ToggleSnapToGeometryAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.TreeViewer;

import com.fujitsu.us.visualvno.model.ShapesDiagram;
import com.fujitsu.us.visualvno.parts.ShapesEditPartFactory;
import com.fujitsu.us.visualvno.parts.ShapesTreeEditPartFactory;

/**
 * A graphical editor with flyout palette that can edit .shapes files. The
 * binding between the .shapes file extension and this editor is done in
 * plugin.xml
 */
public class ShapesEditor extends GraphicalEditorWithFlyoutPalette
{

	/** This is the root of the editor's model. */
	private ShapesDiagram		diagram;

	/** Palette component, holding the tools and shapes. */
	private static PaletteRoot	PALETTE_MODEL;

	/** Create a new ShapesEditor instance. Called by the Workspace. */
	public ShapesEditor()
	{
		setEditDomain(new DefaultEditDomain(this));
	}

	/**
	 * Configure the graphical viewer before it receives contents.
	 */
	@Override
	protected void configureGraphicalViewer()
	{
		super.configureGraphicalViewer();

		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new ShapesEditPartFactory());
		viewer.setRootEditPart   (new ScalableFreeformRootEditPart());
		viewer.setKeyHandler     (new GraphicalViewerKeyHandler(viewer));

		// configure the context menu provider
		viewer.setContextMenu(new ShapesEditorContextMenuProvider(viewer, 
		                                                          getActionRegistry()));
		
		getActionRegistry().registerAction(new ToggleGridAction(getGraphicalViewer()));
        getActionRegistry().registerAction(new ToggleSnapToGeometryAction(getGraphicalViewer()));
	}

	@Override
	public void commandStackChanged(EventObject event)
	{
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}

	private void createOutputStream(OutputStream os) throws IOException
	{
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(getModel());
		oos.close();
	}

	@Override
	protected PaletteViewerProvider createPaletteViewerProvider()
	{
		return new PaletteViewerProvider(getEditDomain())
		{
			@Override
			protected void configurePaletteViewer(PaletteViewer viewer)
			{
				super.configurePaletteViewer(viewer);
				
				// create a drag source listener for this palette viewer
				// together with an appropriate transfer drop target listener,
				// this will enable model element creation by dragging a
				// CombinatedTemplateCreationEntries from the palette into the editor
				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
			}
		};
	}

	/**
	 * Create a transfer drop target listener.
	 * When using a CombinedTemplateCreationEntry tool in the palette, 
	 * this will enable model element creation by dragging from the palette.
	 */
	private TransferDropTargetListener createTransferDropTargetListener()
	{
		return new TemplateTransferDropTargetListener(getGraphicalViewer())
		{
			@Override
			protected CreationFactory getFactory(Object template) {
				return new SimpleFactory((Class) template);
			}
		};
	}

	@Override
	public void doSave(IProgressMonitor monitor)
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try
		{
			createOutputStream(out);
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.setContents(new ByteArrayInputStream(out.toByteArray()), 
			                 true, false, monitor);
			getCommandStack().markSaveLocation();
		}
		catch(CoreException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSaveAs()
	{
		// Show a SaveAs dialog
		Shell shell = getSite().getWorkbenchWindow().getShell();
		SaveAsDialog dialog = new SaveAsDialog(shell);
		dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
		dialog.open();

		IPath path = dialog.getResult();
		if(path != null)
		{
			// try to save the editor's contents under a different file name
			final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			try
			{
				ProgressMonitorDialog dlg = new ProgressMonitorDialog(shell);
				dlg.run(false, false, 
				        new WorkspaceModifyOperation()
						{
							@Override
							public void execute(final IProgressMonitor monitor)
							{
								try
								{
									ByteArrayOutputStream out = new ByteArrayOutputStream();
									createOutputStream(out);
									file.create(new ByteArrayInputStream(out.toByteArray()),
																		 true, monitor);
								}
								catch(CoreException e) {
									e.printStackTrace();
								}
								catch(IOException e) {
									e.printStackTrace();
								}
							}
						}
				);
				
				// set input to the new file
				setInput(new FileEditorInput(file));
				getCommandStack().markSaveLocation();
			}
			catch(InterruptedException e)
			{
				// should not happen, since the monitor dialog is not cancelable
				e.printStackTrace();
			}
			catch(InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Object getAdapter(Class type)
	{
		if(type == IContentOutlinePage.class)
			return new ShapesOutlinePage(new TreeViewer());
		return super.getAdapter(type);
	}

	ShapesDiagram getModel() {
		return diagram;
	}

	@Override
	protected PaletteRoot getPaletteRoot()
	{
		if(PALETTE_MODEL == null)
			PALETTE_MODEL = ShapesEditorPaletteFactory.createPalette();
		return PALETTE_MODEL;
	}

	private void handleLoadException(Exception e)
	{
		System.err.println("** Load failed. Using default model. **");
		e.printStackTrace();
		diagram = new ShapesDiagram();
	}

	@Override
	protected void initializeGraphicalViewer()
	{
		super.initializeGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setContents(getModel()); // set the contents of this editor

		// listen for dropped parts
		viewer.addDropTargetListener(createTransferDropTargetListener());
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	protected void setInput(IEditorInput input)
	{
		super.setInput(input);
		try
		{
			IFile file = ((IFileEditorInput) input).getFile();
			ObjectInputStream in = new ObjectInputStream(file.getContents());
			diagram = (ShapesDiagram) in.readObject();
			in.close();
			setPartName(file.getName());
		}
		catch(IOException e) {
			handleLoadException(e);
		}
		catch(CoreException e) {
			handleLoadException(e);
		}
		catch(ClassNotFoundException e) {
			handleLoadException(e);
		}
	}

	/**
	 * Creates an outline page for this editor.
	 */
	public class ShapesOutlinePage extends ContentOutlinePage
	{
		public ShapesOutlinePage(EditPartViewer viewer)	{
			super(viewer);
		}

		@Override
		public void createControl(Composite parent)
		{
			// create outline viewer page
			getViewer().createControl(parent);
			
			// configure outline viewer
			getViewer().setEditDomain(getEditDomain());
			getViewer().setEditPartFactory(new ShapesTreeEditPartFactory());
			
			// configure & add context menu to viewer
			ContextMenuProvider cmProvider 
				= new ShapesEditorContextMenuProvider(getViewer(),
													  getActionRegistry());
			getViewer().setContextMenu(cmProvider);
			getSite().registerContextMenu("com.fujitsu.us.outline.contextmenu",
										  cmProvider, 
										  getSite().getSelectionProvider());
			
			// hook up outline viewer
			getSelectionSynchronizer().addViewer(getViewer());
			
			// initialize outline viewer with model
			getViewer().setContents(getModel());
		}

		@Override
		public void dispose()
		{
			// unhook outline viewer
			getSelectionSynchronizer().removeViewer(getViewer());
			super.dispose();
		}

		@Override
		public Control getControl() {
			return getViewer().getControl();
		}

		@Override
		public void init(IPageSite pageSite)
		{
			super.init(pageSite);
			ActionRegistry registry = getActionRegistry();
			IActionBars bars = pageSite.getActionBars();
			String id = ActionFactory.UNDO.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			id = ActionFactory.REDO.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			id = ActionFactory.DELETE.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
		}
	}

}
