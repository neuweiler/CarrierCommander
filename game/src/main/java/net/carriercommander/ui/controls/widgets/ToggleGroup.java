package net.carriercommander.ui.controls.widgets;

import com.jme3.scene.Node;
import com.simsilica.lemur.*;

public class ToggleGroup extends Container {
	@Override
	public <T extends Node> T addChild(T child, Object... constraints ) {
		super.addChild(child, constraints);

		if (child instanceof ToggleButton toggleButton) {
			toggleButton.addCommands(Button.ButtonAction.Down, this::toggle);
		}

		return child;
	}

	public void toggle(Button source) {
		this.children.forEach(child -> {
			if (child != source && child instanceof ToggleButton toggleButton) {
				if (toggleButton.isSelected()) {
					toggleButton.setSelected(false);
				}
			}
		});
	}
}
