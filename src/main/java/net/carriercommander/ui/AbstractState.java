package net.carriercommander.ui;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;

/**
 * The super-class for all states in Carrier Command.
 */
public abstract class AbstractState extends BaseAppState {

	/**
	 * Calculate a scale factor for UI components for the current resolution based on a 720 resolution.
	 * @return the scale factor to be used to scale UI components
	 */
	protected float getStandardScale() {
		int height = getApplication().getCamera().getHeight();
		return height / 720f;
	}

	/**
	 * Retrieve the scene graph's root node.
	 * @return the root node
	 */
	public Node getRootNode() {
		return ((SimpleApplication) getApplication()).getRootNode();
	}

}
