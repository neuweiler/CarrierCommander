package net.carriercommander.ui.controls;

import com.jme3.app.Application;
import net.carriercommander.ui.ControlState;
import net.carriercommander.ui.controls.carrier.StateCarrier;
import net.carriercommander.ui.controls.defense.StateDefense;
import net.carriercommander.ui.controls.manta.StateManta;
import net.carriercommander.ui.controls.walrus.StateWalrus;
import net.carriercommander.ui.controls.widgets.ToggleGroup;
import net.carriercommander.ui.controls.widgets.ToggleImageButton;
import net.carriercommander.ui.controls.widgets.Window;

public class StateMainControls extends ControlState {

	@Override
	protected void initialize(Application app) {
		super.initialize(app, StateCarrier.class, StateDefense.class, StateWalrus.class, StateManta.class);

		window = new Window();

		ToggleGroup group = window.addChild(new ToggleGroup());
		group.addChild(new ToggleImageButton("/Interface/hud/carrier/carrier.png", this, "carrier")).setSelected(true);
		group.addChild(new ToggleImageButton("/Interface/hud/defense/defense.png", this, "weapons"));
		group.addChild(new ToggleImageButton("/Interface/hud/walrus/walrus.png", this, "walrus"));
		group.addChild(new ToggleImageButton("/Interface/hud/manta/manta.png", this, "manta"));
		group.addChild(new ToggleImageButton("/Interface/hud/game/game.png", this, "game"));

		scaleAndPosition(app.getCamera(), 0, 1);
	}

	private void carrier() {
		switchToState(StateCarrier.class);
	}

	private void weapons() {
		switchToState(StateDefense.class);
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
