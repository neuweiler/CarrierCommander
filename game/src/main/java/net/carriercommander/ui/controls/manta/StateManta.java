package net.carriercommander.ui.controls.manta;

import com.jme3.app.Application;
import net.carriercommander.ui.ControlState;
import net.carriercommander.ui.controls.widgets.ToggleGroup;
import net.carriercommander.ui.controls.widgets.ToggleImageButton;
import net.carriercommander.ui.controls.widgets.Window;

public class StateManta extends ControlState {


	@Override
	protected void initialize(Application app) {
		super.initialize(app, StateNavigation.class, StateMap.class,
				StateEquip.class, StateHangar.class, StateStatus.class);

		window = new Window();

		ToggleGroup group = window.addChild(new ToggleGroup());
		group.addChild(new ToggleImageButton("/Interface/hud/shared/control.png", this, "navigation")).setSelected(true);
		group.addChild(new ToggleImageButton("/Interface/hud/shared/map.png", this, "map"));
		group.addChild(new ToggleImageButton("/Interface/hud/manta/mantaEquip.png", this, "equip"));
		group.addChild(new ToggleImageButton("/Interface/hud/manta/mantaHangar.png", this, "hangar"));
		group.addChild(new ToggleImageButton("/Interface/hud/shared/status.png", this, "status"));

		scaleAndPosition(app.getCamera(), 1, 1);
	}

	private void navigation() {
		switchToState(StateNavigation.class);
	}

	private void map() {
		switchToState(StateMap.class);
	}

	private void equip() {
		switchToState(StateEquip.class);
	}

	private void hangar() {
		switchToState(StateHangar.class);
	}

	private void status() {
		switchToState(StateStatus.class);
	}

}