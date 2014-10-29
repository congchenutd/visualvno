package com.fujitsu.us.visualvno.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
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
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.MatchSizeAction;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ToggleSnapToGeometryAction;
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
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.actions.DecommissionAction;
import com.fujitsu.us.visualvno.actions.InitAction;
import com.fujitsu.us.visualvno.actions.StartAction;
import com.fujitsu.us.visualvno.actions.StopAction;
import com.fujitsu.us.visualvno.actions.VerifyAction;
import com.fujitsu.us.visualvno.model.DiagramModel;
import com.fujitsu.us.visualvno.parts.factories.ShapesEditPartFactory;
import com.fujitsu.us.visualvno.parts.factories.ShapesTreeEditPartFactory;

public class VNOEditor extends GraphicalEditorWithFlyoutPalette
{

    private static PaletteRoot PALETTE_MODEL;   // palette
    private DiagramModel       _diagram;        // root of the model
    private IEditorInput       _input;

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
        ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
        viewer.setRootEditPart      (rootEditPart);
        viewer.setEditPartFactory   (new ShapesEditPartFactory());
        viewer.setKeyHandler        (new GraphicalViewerKeyHandler(viewer));

        // configure the context menu provider
        ContextMenuProvider menuProvider = new EditorContextMenuProvider(
                                                    viewer, getActionRegistry());
        viewer.setContextMenu(menuProvider);
        getSite().registerContextMenu(menuProvider, viewer);
        
        // Zoom actions
        IAction zoomIn  = new ZoomInAction (rootEditPart.getZoomManager());
        IAction zoomOut = new ZoomOutAction(rootEditPart.getZoomManager());
        getActionRegistry().registerAction(zoomIn);
        getActionRegistry().registerAction(zoomOut);

        IHandlerService service = (IHandlerService) getSite().getService(IHandlerService.class);
        service.activateHandler(zoomIn.getActionDefinitionId(),
                                new ActionHandler(zoomIn));
        service.activateHandler(zoomOut.getActionDefinitionId(),
                                new ActionHandler(zoomOut));
        
        List<String> zoomLevels = new ArrayList<String>(3);
        zoomLevels.add(ZoomManager.FIT_ALL);
        zoomLevels.add(ZoomManager.FIT_WIDTH);
        zoomLevels.add(ZoomManager.FIT_HEIGHT);
        rootEditPart.getZoomManager().setZoomLevelContributions(zoomLevels);
        
        // register snap and grid actions
        IAction snapAction = new ToggleSnapToGeometryAction(viewer);
        getActionRegistry().registerAction(snapAction);

        IAction showGrid = new ToggleGridAction(viewer);
        getActionRegistry().registerAction(showGrid);
        
        loadProperties();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    protected void createActions()
    {
        super.createActions();
        ActionRegistry registry = getActionRegistry();
        
        IAction action = new MatchSizeAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        
        action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.LEFT);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.RIGHT);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.TOP);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.BOTTOM);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.CENTER);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.MIDDLE);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());
        
        registry.registerAction(new InitAction());
        registry.registerAction(new VerifyAction());
        registry.registerAction(new StartAction());
        registry.registerAction(new StopAction());
        registry.registerAction(new DecommissionAction());
    }
    
    protected void saveProperties()
    {
        DiagramModel    diagram = getModel();
        GraphicalViewer viewer  = getGraphicalViewer();
        
        diagram.setGridEnabled(
                ((Boolean) viewer.getProperty(
                        SnapToGrid.PROPERTY_GRID_ENABLED)).booleanValue());
        
        diagram.setSnapToGeometry(
                ((Boolean) viewer.getProperty(
                        SnapToGeometry.PROPERTY_SNAP_ENABLED)).booleanValue());
        
        ZoomManager manager = (ZoomManager) viewer.getProperty(
                ZoomManager.class.toString());
        if(manager != null)
            diagram.setZoom(manager.getZoom());
    }
    
    protected void loadProperties()
    {
        DiagramModel    diagram = getModel();
        GraphicalViewer viewer  = getGraphicalViewer();
        if(viewer == null)
            return;
        
        // Snap to Geometry property
        viewer.setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED,
                new Boolean(diagram.isSnapToGeometryEnabled()));

        // Grid properties
        viewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED,
                new Boolean(diagram.isGridEnabled()));
        
        // keep grid visibility and enablement in sync
        viewer.setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE,
                new Boolean(diagram.isGridEnabled()));

        // Zoom
        ZoomManager manager = (ZoomManager) viewer.getProperty(
                ZoomManager.class.toString());
        if (manager != null)
            manager.setZoom(diagram.getZoom());
        
        // Scroll-wheel Zoom
        viewer.setProperty(
                MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1),
                MouseWheelZoomHandler.SINGLETON);
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
                viewer.addDragSourceListener(new TemplateTransferDragSourceListener(
                        viewer));
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
        saveProperties();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try
        {
            createOutputStream(out);
            IFile file = ((IFileEditorInput) getEditorInput()).getFile();
            file.setContents(
                new ByteArrayInputStream(out.toByteArray()), true, false, monitor);
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
                new ProgressMonitorDialog(shell).run(
                    false, // don't fork
                    false, // not cancelable
                    new WorkspaceModifyOperation()
                    {
                        @Override
                        public void execute(final IProgressMonitor monitor)
                        {
                            saveProperties();
                            try
                            {
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                createOutputStream(out);
                                file.create(
                                    new ByteArrayInputStream(out.toByteArray()),
                                    true, monitor);
                            }
                            catch(CoreException | IOException e) {
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

    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class type)
    {
        if(type == IContentOutlinePage.class)
            return new OutlinePage(new TreeViewer());
        if (type == ZoomManager.class)
            return getGraphicalViewer().getProperty(ZoomManager.class.toString());
        return super.getAdapter(type);
    }

    public DiagramModel getModel() {
        return _diagram;
    }

    @Override
    protected PaletteRoot getPaletteRoot()
    {
        if(PALETTE_MODEL == null)
            PALETTE_MODEL = EditorPaletteFactory.createPalette();
        return PALETTE_MODEL;
    }

    @Override
    protected void initializeGraphicalViewer()
    {
        super.initializeGraphicalViewer();
        showDiagram(); // set the contents of this editor

        // listen for dropped parts
        getGraphicalViewer().addDropTargetListener(
                                    createTransferDropTargetListener());
    }

    @Override
    public boolean isSaveAsAllowed() {
        return true;
    }

    @Override
    protected void setInput(IEditorInput input)
    {
        super.setInput(input);
        _input = input;
        doSetInput();
    }
    
    public void doSetInput()
    {
        super.setInput(_input);
        try
        {
            IFile file = ((IFileEditorInput) _input).getFile();
            ObjectInputStream in = new ObjectInputStream(file.getContents());
            _diagram = (DiagramModel) in.readObject();
            in.close();
            setPartName(file.getName());
            loadProperties();
        }
        catch(IOException | CoreException | ClassNotFoundException e)
        {
            System.err.println("** Load failed. Using default model. **");
            e.printStackTrace();
            _diagram = new DiagramModel();
        }
    }
    
    public void showDiagram() {
        getGraphicalViewer().setContents(getModel());
    }

    /**
     * Creates an outline pagebook for this editor.
     */
    public class OutlinePage extends ContentOutlinePage
    {
        private PageBook  _pageBook;
        private IAction   _showOutlineAction;
        private IAction   _showOverviewAction;
        private Control   _outline;
        private Canvas    _overview;
        private Thumbnail _thumbnail;
        
        static final int ID_OUTLINE  = 0;
        static final int ID_OVERVIEW = 1;
        
        public OutlinePage(EditPartViewer viewer) {
            super(viewer);
        }

        /**
         * hook actions to the outline view 
         */
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
        
        @Override
        public void createControl(Composite parent)
        {
            _pageBook = new PageBook(parent, SWT.NONE);
            
            // thumbnail
            _overview = new Canvas(_pageBook, SWT.NONE);
            initThumbnail();
            
            // create outline viewer page
            _outline = getViewer().createControl(_pageBook);
            
            // configure outline viewer
            initOutline();
            
            // hook outline viewer
            getSelectionSynchronizer().addViewer(getViewer());
            getViewer().setContents(getModel());
        }
        
        private void initOutline()
        {
            getViewer().setEditDomain(getEditDomain());
            getViewer().setEditPartFactory(new ShapesTreeEditPartFactory());
            
            // context menu
            ContextMenuProvider menuProvider 
                = new EditorContextMenuProvider(getViewer(),
                                                getActionRegistry());
            getViewer().setContextMenu(menuProvider);
            getSite().registerContextMenu("com.fujitsu.us.outline.contextmenu",
                                          menuProvider, 
                                          getSite().getSelectionProvider());
            
            getViewer().addDropTargetListener((TransferDropTargetListener)
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

        @Override
        public void dispose()
        {
            // unhook outline viewer
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

    }

}