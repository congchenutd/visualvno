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
import com.fujitsu.us.visualvno.model.EllipticalShape;
import com.fujitsu.us.visualvno.model.RectangularShape;

/**
 * Utility class that can create a GEF Palette.
 */
final class ShapesEditorPaletteFactory
{
	/** Create the "Shapes" drawer. */
	private static PaletteContainer createShapesDrawer()
	{
		PaletteDrawer componentsDrawer = new PaletteDrawer("Elements");

		componentsDrawer.add(new CombinedTemplateCreationEntry(
			    "Switch",
				"Create a Switch",
				EllipticalShape.class,
				new SimpleFactory(EllipticalShape.class),
				ImageDescriptor.createFromFile(ShapesPlugin.class,
											   "icons/ellipse16.gif"),
				ImageDescriptor.createFromFile(ShapesPlugin.class,
											   "icons/ellipse24.gif")));

		componentsDrawer.add(new CombinedTemplateCreationEntry(
						"Host",
						"Create a Host",
						RectangularShape.class,
						new SimpleFactory(RectangularShape.class),
						ImageDescriptor.createFromFile(ShapesPlugin.class,
													   "icons/rectangle16.gif"),
						ImageDescriptor.createFromFile(ShapesPlugin.class,
													   "icons/rectangle24.gif")));

		return componentsDrawer;
	}

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

		// Add (solid-line) connection tool
		toolbar.add(new ConnectionCreationToolEntry(
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
												   "icons/connection_s16.gif"),
					ImageDescriptor.createFromFile(ShapesPlugin.class,
												   "icons/connection_s24.gif")));
		return toolbar;
	}

	private ShapesEditorPaletteFactory() {}

}
