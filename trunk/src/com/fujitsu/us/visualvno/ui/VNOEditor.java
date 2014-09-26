package com.fujitsu.us.visualvno.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.actions.AddPortAction;
import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.parts.factories.ShapesEditPartFactory;
import com.fujitsu.us.visualvno.parts.factories.ShapesTreeEditPartFactory;

/**
 * A graphical editor with flyout palette that can edit .vno files.
 * The binding between ".vno" and the editor is done in plugin.xml
 */
public class VNOEditor extends GraphicalEditorWithFlyoutPalette
{

	/** Root of the editor's model. */
	private DiagramModel _diagram;

	/** Palette component, holding the tools and shapes. */
	private static PaletteRoot PALETTE_MODEL;

	/** Called by the Workspace. */
	public VNOEditor() {
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
		ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
		viewer.setRootEditPart   (root);
		viewer.setEditPartFactory(new ShapesEditPartFactory());
		viewer.setKeyHandler     (new GraphicalViewerKeyHandler(viewer));

		// configure the context menu provider
		ContextMenuProvider menuProvider = new VNOEditorContextMenuProvider(
		                                           viewer, getActionRegistry());
		viewer.setContextMenu(menuProvider);
		
		// Zoom actions
		IAction zoomIn  = new ZoomInAction (root.getZoomManager());
        IAction zoomOut = new ZoomOutAction(root.getZoomManager());
        getActionRegistry().registerAction(zoomIn);
        getActionRegistry().registerAction(zoomOut);

        IHandlerService service = (IHandlerService) getSite().getService(IHandlerService.class);
        service.activateHandler(zoomIn.getActionDefinitionId(),
                                new ActionHandler(zoomIn));
        service.activateHandler(zoomOut.getActionDefinitionId(),
                                new ActionHandler(zoomOut));
        
        // scroll-wheel Zoom
        getGraphicalViewer().setProperty(
            MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1),
            MouseWheelZoomHandler.SINGLETON);
	}

	@Override
    protected void createActions()
	{
        AddPortAction action = new AddPortAction(this);
        getActionRegistry().registerAction(action);
        getSelectionActions().add(action.getId());
        super.createActions();
    }
	
	@Override
	public void commandStackChanged(EventObject event)
	{
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
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
				return new SimpleFactory((Class<?>) template);
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
		catch(CoreException | IOException e) {
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
				});
				
				// set input to the new file
				setInput(new FileEditorInput(file));
				getCommandStack().markSaveLocation();
			}
			catch(InterruptedException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void createOutputStream(OutputStream os) throws IOException
    {
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(getModel());
        oos.close();
    }

	@SuppressWarnings("rawtypes")
    @Override
	public Object getAdapter(Class type)
	{
	    // adapt to an OutlinePage
		if(type == IContentOutlinePage.class)
			return new OutlinePage(new TreeViewer());
		return super.getAdapter(type);
	}

	public DiagramModel getModel() {
		return _diagram;
	}

	/**
	 * Create the palette
	 */
	@Override
	protected PaletteRoot getPaletteRoot()
	{
		if(PALETTE_MODEL == null)
			PALETTE_MODEL = VNOEditorPaletteFactory.createPalette();
		return PALETTE_MODEL;
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

	/**
	 * Read file
	 */
	@Override
	protected void setInput(IEditorInput input)
	{
		super.setInput(input);
		try
		{
			IFile file = ((IFileEditorInput) input).getFile();
			ObjectInputStream in = new ObjectInputStream(file.getContents());
			_diagram = (DiagramModel) in.readObject();
			in.close();
			setPartName(file.getName());
		}
		catch(IOException | CoreException | ClassNotFoundException e) {
			System.err.println("Load failed. Using default model.");
			e.printStackTrace();
			_diagram = new DiagramModel();
		}
	}

	/**
	 * Outline page with an outline view and an overview (thumbnail)
	 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
	 */
	class OutlinePage extends ContentOutlinePage
	{
        private PageBook    _pageBook;
        private IAction     _showOutlineAction;
        private IAction     _showOverviewAction;
        private Control     _outline;
        private Canvas      _overview;
        private Thumbnail   _thumbnail;
        
        static final int ID_OUTLINE     = 0;
        static final int ID_OVERVIEW    = 1;
        
        public OutlinePage(EditPartViewer viewer) {
            super(viewer);
        }

        @Override
        public void init(IPageSite pageSite)
        {
            super.init(pageSite);
            
            // hook actions to the outline view 
            ActionRegistry registry = getActionRegistry();
            IActionBars bars = pageSite.getActionBars();
            
            String id = ActionFactory.UNDO.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));
            
            id = ActionFactory.REDO.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));
            
            id = ActionFactory.DELETE.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));
            
            bars.updateActionBars(); 
        }

        @Override
        public void createControl(Composite parent)
        {
            _pageBook = new PageBook(parent, SWT.NONE);

            // thumbnail
            _overview = new Canvas(_pageBook, SWT.NONE);
            initThumbnail();
            
            // outline
            _outline  = getViewer().createControl(_pageBook);
            initOutline();
            getSelectionSynchronizer().addViewer(getViewer());
            getViewer().setContents(getModel());
        }

        @Override
        public void dispose()
        {
            // unhook OutlineViewer
            getSelectionSynchronizer().removeViewer(getViewer());
            
            if(_thumbnail != null)
            {
                _thumbnail.deactivate();
                _thumbnail = null;
            }
            super.dispose();
        }

        @Override
        public Control getControl() {
            return _pageBook;
        }

        private void initOutline()
        {
            getViewer().setEditDomain(getEditDomain());
            getViewer().setEditPartFactory(new ShapesTreeEditPartFactory());
            
            // context menu
            ContextMenuProvider menuProvider 
                = new VNOEditorContextMenuProvider(getViewer(),
                                                      getActionRegistry());
            getViewer().setContextMenu(menuProvider);
            getSite().registerContextMenu("com.fujitsu.us.outline.contextmenu",
                                          menuProvider, 
                                          getSite().getSelectionProvider());
            
            getViewer().addDropTargetListener(
                  (TransferDropTargetListener)
                  new TemplateTransferDropTargetListener(getViewer()));
            
            // 2 actions: "Show outline" and "Show thumbnail"
            IToolBarManager tbm = getSite().getActionBars().getToolBarManager();
            _showOutlineAction = new Action() {
                @Override
                public void run() {
                    showPage(ID_OUTLINE);
                }
            };
            _showOutlineAction.setImageDescriptor(
                 ImageDescriptor.createFromFile(VisualVNOPlugin.class, 
                                                "icons/outline.gif"));
            _showOutlineAction.setToolTipText("Show outline");
            tbm.add(_showOutlineAction);
            
            _showOverviewAction = new Action() {
                @Override
                public void run() {
                    showPage(ID_OVERVIEW);
                }
            };
            _showOverviewAction.setImageDescriptor(
                  ImageDescriptor.createFromFile(VisualVNOPlugin.class, 
                                                 "icons/overview.gif"));
            _showOverviewAction.setToolTipText("Show thumbnail");
            tbm.add(_showOverviewAction);
            
            // default page
            showPage(ID_OUTLINE);
        }

        private void initThumbnail()
        {
            LightweightSystem lws = new LightweightSystem(_overview);
            RootEditPart rep = getGraphicalViewer().getRootEditPart();
            if(rep instanceof ScalableFreeformRootEditPart)
            {
                ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) rep;
                _thumbnail = new ScrollableThumbnail((Viewport) root.getFigure());
                _thumbnail.setBorder(new MarginBorder(3));
                _thumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
                lws.setContents(_thumbnail);
            }
        }
        
        private void showPage(int id)
        {
            if(id == ID_OUTLINE)
            {
                _showOutlineAction .setChecked(true);
                _showOverviewAction.setChecked(false);
                _pageBook.showPage(_outline);
                _thumbnail.setVisible(false);
            }
            else if(id == ID_OVERVIEW)
            {
                _showOutlineAction .setChecked(false);
                _showOverviewAction.setChecked(true);
                _pageBook.showPage(_overview);
                _thumbnail.setVisible(true);
            }
        }

    }

}
