package org.eclipse.gef.examples.shapes;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The plugin class (singleton).
 */
public class VisualVNOPlugin extends AbstractUIPlugin
{
    private static VisualVNOPlugin _instance;

    public static VisualVNOPlugin getDefault() {
        return _instance;
    }

    public VisualVNOPlugin()
    {
        if(_instance == null)
            _instance = this;
    }

}