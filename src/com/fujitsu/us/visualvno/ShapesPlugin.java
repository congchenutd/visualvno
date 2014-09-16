package com.fujitsu.us.visualvno;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The plugin class (singleton).
 */
public class ShapesPlugin extends AbstractUIPlugin {

	/** Single plugin instance. */
	private static ShapesPlugin singleton;

	/**
	 * Returns the shared plugin instance.
	 */
	public static ShapesPlugin getDefault() {
		return singleton;
	}

	/**
	 * The constructor.
	 */
	public ShapesPlugin() {
		if (singleton == null) {
			singleton = this;
		}
	}

}