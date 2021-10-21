package net.carriercommander.ui;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import com.simsilica.lemur.Container;

/**
 * A state class for windows which attach/detach automatically from the GUI node when enabled/disabled.
 * If used, the variable "window" must be set in the initialize() phase of the subclass.
 */
public abstract class WindowState extends AbstractState {

	protected Container window;

	@Override
	protected void onEnable() {
		Node gui = ((SimpleApplication) getApplication()).getGuiNode();
		gui.attachChild(window);
	}

	@Override
	protected void onDisable() {
		if (window != null) {
			window.removeFromParent();
		}
	}
}
