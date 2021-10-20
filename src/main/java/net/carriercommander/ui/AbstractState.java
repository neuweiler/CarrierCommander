package net.carriercommander.ui;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;

public abstract class AbstractState extends BaseAppState {

	protected float getStandardScale() {
		int height = getApplication().getCamera().getHeight();
		return height / 720f;
	}

	protected Node getRootNode() {
		return ((SimpleApplication) getApplication()).getRootNode();
	}

}
