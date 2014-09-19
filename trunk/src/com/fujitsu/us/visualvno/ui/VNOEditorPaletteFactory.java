package com.fujitsu.us.visualvno.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.model.ConnectionModel;
import com.fujitsu.us.visualvno.model.SwitchModel;
import com.fujitsu.us.visualvno.model.HostModel;

/**
 * Utility class that creates a GEF Palette.
 */
public class VNOEditorPaletteFactory
{
    /**
     * Creates a PaletteRoot and adds all palette elements.
     */
    public static PaletteRoot createPalette()
    {
        PaletteRoot palette = new PaletteRoot();
        palette.add(createToolsGroup(palette));
        palette.add(createShapesDrawer());
        return palette;
    }
    
	/** Create the "Elements" drawer. */
	private static PaletteContainer createShapesDrawer()
	{
		PaletteDrawer componentsDrawer = new PaletteDrawer("Elements");

		componentsDrawer.add(new CombinedTemplateCreationEntry(
		    "Switch",
			"Create a Switch",
			SwitchModel.class,
			new SimpleFactory(SwitchModel.class),
			ImageDescriptor.createFromFile(VisualVNOPlugin.class,
										   SwitchModel.imageFileSmall),
			ImageDescriptor.createFromFile(VisualVNOPlugin.class,
										   SwitchModel.imageFileBig)));

		componentsDrawer.add(new CombinedTemplateCreationEntry(
			"Host",
			"Create a Host", 
			HostModel.class, 
			new SimpleFactory(HostModel.class),
			ImageDescriptor.createFromFile(VisualVNOPlugin.class,
										   HostModel.imageFileSmall), 
			ImageDescriptor.createFromFile(VisualVNOPlugin.class, 
										   HostModel.imageFileBig)));
		
        componentsDrawer.add(new ConnectionCreationToolEntry(
            "Link",
            "Create a Link",
            new CreationFactory()
            {
                @Override
                public Object getNewObject() {
                    return null;
                }

                @Override
                public Object getObjectType() {
                    return ConnectionModel.SOLID_CONNECTION;
                }
            },
            ImageDescriptor.createFromFile(VisualVNOPlugin.class,
                                           ConnectionModel.imageFileSmall),
            ImageDescriptor.createFromFile(VisualVNOPlugin.class,
                                           ConnectionModel.imageFileBig)));

		return componentsDrawer;
	}

	/** Create the "Tools" group. */
	private static PaletteContainer createToolsGroup(PaletteRoot palette)
	{
		PaletteToolbar toolbar = new PaletteToolbar("Tools");

		// Add a selection tool to the group
		ToolEntry tool = new PanningSelectionToolEntry();
		toolbar.add(tool);
		palette.setDefaultEntry(tool);

		// Add a marquee tool to the group
		toolbar.add(new MarqueeToolEntry());

		return toolbar;
	}

	private VNOEditorPaletteFactory() {}

}
