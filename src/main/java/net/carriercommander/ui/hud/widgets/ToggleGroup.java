package net.carriercommander.ui.hud.widgets;

import com.jme3.scene.Node;
import com.simsilica.lemur.Container;

public class ToggleGroup extends Container {

	@Override
	public <T extends Node> T addChild(T child, Object... constraints ) {
		super.addChild(child, constraints);

		//TODO replace action with an interceptor or something simmilar and set styles

		return child;
	}

}
