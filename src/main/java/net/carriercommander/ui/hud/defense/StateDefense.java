package net.carriercommander.ui.hud.defense;

import com.jme3.app.Application;
import net.carriercommander.ui.ControlState;
import net.carriercommander.ui.hud.widgets.ToggleGroup;
import net.carriercommander.ui.hud.widgets.ToggleImageButton;
import net.carriercommander.ui.hud.widgets.Window;

public class StateDefense extends ControlState {

	@Override
	protected void initialize(Application app) {
		super.initialize(app, StateLaser.class, StateFlare.class,
				StateMissile.class, StateDrone.class, StateStatus.class);

		window = new Window(getApplication().getCamera().getWidth() - 55, getApplication().getCamera().getHeight());

		ToggleGroup group = window.addChild(new ToggleGroup());
		group.addChild(new ToggleImageButton("/Interface/hud/defense/laser.png", this, "laser")).setSelected(true);
		group.addChild(new ToggleImageButton("/Interface/hud/defense/flare.png", this, "flare"));
		group.addChild(new ToggleImageButton("/Interface/hud/defense/missileCarrier.png", this, "missile"));
		group.addChild(new ToggleImageButton("/Interface/hud/defense/drones.png", this, "drone"));
		group.addChild(new ToggleImageButton("/Interface/hud/shared/status.png", this, "status"));
	}

	private void laser() {
		switchToState(StateLaser.class);
	}

	private void flare() {
		switchToState(StateFlare.class);
	}

	private void missile() {
		switchToState(StateMissile.class);
	}

	private void drone() {
		switchToState(StateDrone.class);
	}

	private void status() {
		switchToState(StateStatus.class);
	}

}
