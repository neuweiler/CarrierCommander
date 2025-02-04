package net.carriercommander.ui.hud.widgets;

import com.jme3.scene.Node;
import com.simsilica.lemur.*;

public class ToggleGroup extends Container {
	@Override
	public <T extends Node> T addChild(T child, Object... constraints ) {
		super.addChild(child, constraints);

		if (child instanceof ToggleButton) {
			ToggleButton toggleButton = (ToggleButton) child;
			toggleButton.addCommands(Button.ButtonAction.Down, new Command<Button>() {
				@Override
				public void execute(Button source) {
					toggle(source);
				}
			});
		}

		return child;
	}

	public void toggle(Button source) {
		this.children.forEach(child -> {
			if (child != source && child instanceof ToggleButton) {
				ToggleButton toggleButton = (ToggleButton) child;
				if (toggleButton.isSelected()) {
					toggleButton.setSelected(false);
				}
			}
		});
	}
}
