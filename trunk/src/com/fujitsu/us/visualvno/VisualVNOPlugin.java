package com.fujitsu.us.visualvno;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The plugin class (singleton).
 */
public class VisualVNOPlugin extends AbstractUIPlugin {

	/** Single plugin instance. */
	private static VisualVNOPlugin singleton;

	/**
	 * Returns the shared plugin instance.
	 */
	public static VisualVNOPlugin getDefault() {
		return singleton;
	}

	/**
	 * The constructor.
	 */
	public VisualVNOPlugin() {
		if (singleton == null) {
			singleton = this;
		}
	}

}