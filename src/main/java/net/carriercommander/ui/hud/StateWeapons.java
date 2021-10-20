package net.carriercommander.ui.hud;

import com.jme3.app.Application;
import net.carriercommander.ui.ControlState;
import net.carriercommander.ui.hud.widgets.ImageButton;
import net.carriercommander.ui.hud.widgets.ToggleGroup;
import net.carriercommander.ui.hud.widgets.Window;

public class StateWeapons extends ControlState {

	@Override
	protected void initialize(Application app) {
		super.initialize(app, StateWeaponsLaser.class, StateWeaponsFlare.class,
				StateWeaponsMissile.class, StateWeaponsDrone.class, StateWeaponsStatus.class);

		window = new Window(getApplication().getCamera().getWidth() - 55, getApplication().getCamera().getHeight());

		ToggleGroup group = window.addChild(new ToggleGroup());
		group.addChild(new ImageButton("/Interface/hud/laser.png", this, "laser"));
		group.addChild(new ImageButton("/Interface/hud/flare.png", this, "flare"));
		group.addChild(new ImageButton("/Interface/hud/missileCarrier.png", this, "missile"));
		group.addChild(new ImageButton("/Interface/hud/drones.png", this, "drone"));
		group.addChild(new ImageButton("/Interface/hud/status.png", this, "status"));
	}

	private void laser() {
		switchToState(StateWeaponsLaser.class);
	}

	private void flare() {
		switchToState(StateWeaponsFlare.class);
	}

	private void missile() {
		switchToState(StateWeaponsMissile.class);
	}

	private void drone() {
		switchToState(StateWeaponsDrone.class);
	}

	private void status() {
		switchToState(StateWeaponsStatus.class);
	}

}
