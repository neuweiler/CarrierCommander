package net.carriercommander.ui;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import com.simsilica.lemur.Container;

public abstract class WindowState extends AbstractState {

	protected Container window;

	@Override
	protected void initialize(Application app) {
	}

	@Override
	protected void onEnable() {
		Node gui = ((SimpleApplication) getApplication()).getGuiNode();
		gui.attachChild(window);
	}

	@Override
	protected void onDisable() {
		window.removeFromParent();
	}
}
