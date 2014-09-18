package com.fujitsu.us.visualvno;

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

import com.fujitsu.us.visualvno.model.Connection;
import com.fujitsu.us.visualvno.model.Switch;
import com.fujitsu.us.visualvno.model.Host;

/**
 * Utility class that can create a GEF Palette.
 */
public class ShapesEditorPaletteFactory
{
    /**
     * Creates the PaletteRoot and adds all palette elements.
     */
    static PaletteRoot createPalette()
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
			Switch.class,
			new SimpleFactory(Switch.class),
			ImageDescriptor.createFromFile(ShapesPlugin.class,
										   Switch.imageFileSmall),
			ImageDescriptor.createFromFile(ShapesPlugin.class,
										   Switch.imageFileBig)));

		componentsDrawer.add(new CombinedTemplateCreationEntry(
			"Host",
			"Create a Host", 
			Host.class, 
			new SimpleFactory(Host.class),
			ImageDescriptor.createFromFile(ShapesPlugin.class,
										   Host.imageFileSmall), 
			ImageDescriptor.createFromFile(ShapesPlugin.class, 
										   Host.imageFileBig)));
		
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
                    return Connection.SOLID_CONNECTION;
                }
            },
            ImageDescriptor.createFromFile(ShapesPlugin.class,
                                           Connection.imageFileSmall),
            ImageDescriptor.createFromFile(ShapesPlugin.class,
                                           Connection.imageFileBig)));

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

	private ShapesEditorPaletteFactory() {}

}
