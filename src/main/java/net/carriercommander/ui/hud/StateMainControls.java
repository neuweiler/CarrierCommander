package net.carriercommander.ui.hud;

import com.jme3.app.Application;
import net.carriercommander.ui.ControlState;
import net.carriercommander.ui.hud.widgets.ToggleGroup;
import net.carriercommander.ui.hud.widgets.ToggleImageButton;
import net.carriercommander.ui.hud.widgets.Window;

public class StateMainControls extends ControlState {

	@Override
	protected void initialize(Application app) {
		super.initialize(app, StateCarrier.class, StateWeapons.class, StateWalrus.class, StateManta.class);

		window = new Window(0, getApplication().getCamera().getHeight());

		ToggleGroup group = window.addChild(new ToggleGroup());
		group.addChild(new ToggleImageButton("/Interface/hud/carrier.png", this, "carrier")).setSelected(true);
		group.addChild(new ToggleImageButton("/Interface/hud/weapons.png", this, "weapons"));
		group.addChild(new ToggleImageButton("/Interface/hud/walrus.png", this, "walrus"));
		group.addChild(new ToggleImageButton("/Interface/hud/manta.png", this, "manta"));
		group.addChild(new ToggleImageButton("/Interface/hud/game.png", this, "game"));
	}

	private void carrier() {
		switchToState(StateCarrier.class);
	}

	private void weapons() {
		switchToState(StateWeapons.class);
	}

	private void walrus() {
		switchToState(StateWalrus.class);
	}

	private void manta() {
		switchToState(StateManta.class);
	}

	private void game() {
		//TODO implement
	}

}
