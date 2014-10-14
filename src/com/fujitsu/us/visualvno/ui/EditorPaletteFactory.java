package com.fujitsu.us.visualvno.ui;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.jface.resource.ImageDescriptor;

import com.fujitsu.us.visualvno.VisualVNOPlugin;
import com.fujitsu.us.visualvno.model.HostModel;
import com.fujitsu.us.visualvno.model.LinkModel;
import com.fujitsu.us.visualvno.model.MappingModel;
import com.fujitsu.us.visualvno.model.PortModel;
import com.fujitsu.us.visualvno.model.SwitchModel;

/**
 * Utility class for creating the palette
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
class EditorPaletteFactory
{
    
    static PaletteRoot createPalette()
    {
        PaletteRoot palette = new PaletteRoot();
        palette.add(createTools(palette));
        palette.add(createDrawer());
        return palette;
    }

    private static PaletteContainer createDrawer()
    {
        PaletteDrawer drawer = new PaletteDrawer("Network elements");

        CombinedTemplateCreationEntry switchEntry = 
            new CombinedTemplateCreationEntry(
                "Switch", 
                "Create a switch", 
                SwitchModel.class,
                new SimpleFactory(SwitchModel.class),
                ImageDescriptor.createFromFile(VisualVNOPlugin.class, SwitchModel.SMALL_IMAGE), 
                ImageDescriptor.createFromFile(VisualVNOPlugin.class, SwitchModel.BIG_IMAGE)
        );
        switchEntry.setToolClass(ShapeCreationTool.class);

        CombinedTemplateCreationEntry hostEntry = 
            new CombinedTemplateCreationEntry(
                "Host",
                "Create a host", 
                HostModel.class,
                new SimpleFactory(HostModel.class),
                ImageDescriptor.createFromFile(VisualVNOPlugin.class, HostModel.SMALL_IMAGE),
                ImageDescriptor.createFromFile(VisualVNOPlugin.class, HostModel.BIG_IMAGE)
        );
        hostEntry.setToolClass(ShapeCreationTool.class);
        
        CombinedTemplateCreationEntry portEntry = 
            new CombinedTemplateCreationEntry(
                "Port",
                "Create a port", 
                PortModel.class,
                new SimpleFactory(PortModel.class),
                ImageDescriptor.createFromFile(VisualVNOPlugin.class, PortModel.SMALL_IMAGE),
                ImageDescriptor.createFromFile(VisualVNOPlugin.class, PortModel.BIG_IMAGE)
        );
        
        CreationToolEntry linkEntry = new ConnectionCreationToolEntry(
                "Link",
                "Create a link",
                new SimpleFactory(LinkModel.class),
                ImageDescriptor.createFromFile(VisualVNOPlugin.class, LinkModel.SMALL_IMAGE),
                ImageDescriptor.createFromFile(VisualVNOPlugin.class, LinkModel.BIG_IMAGE)
        );
        linkEntry.setToolClass(LinkCreationTool.class);
        
        CreationToolEntry mappingEntry = new ConnectionCreationToolEntry(
                "Mapping",
                "Create a mapping",
                new SimpleFactory(MappingModel.class),
                ImageDescriptor.createFromFile(VisualVNOPlugin.class, MappingModel.SMALL_IMAGE),
                ImageDescriptor.createFromFile(VisualVNOPlugin.class, MappingModel.BIG_IMAGE)
        );
        
        drawer.add(switchEntry);
        drawer.add(hostEntry);
        drawer.add(portEntry);
        drawer.add(linkEntry);
        drawer.add(mappingEntry);
        return drawer;
    }

    private static PaletteContainer createTools(PaletteRoot palette)
    {
        PaletteToolbar toolbar = new PaletteToolbar("Tools");

        // Add a selection tool to the group
        ToolEntry tool = new PanningSelectionToolEntry();
        toolbar.add(tool);
        palette.setDefaultEntry(tool);

        return toolbar;
    }

}