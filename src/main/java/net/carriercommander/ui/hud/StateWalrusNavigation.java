package net.carriercommander.ui.hud;

import com.jme3.app.Application;
import com.simsilica.lemur.Container;
import net.carriercommander.StatePlayer;
import net.carriercommander.control.PlayerControl;
import net.carriercommander.ui.hud.widgets.ImageButton;
import net.carriercommander.ui.hud.widgets.Window;

public class StateWalrusNavigation extends NavigationState {
	private int lastSelectedWalrus = 0;

	@Override
	protected void initialize(Application app) {
		window = new Window(0, 100);

		Container container = window.addChild(new Container(), 1);
		container.addChild(new ImageButton("/Interface/hud/walrus_small.png", this, "walrus1"));
		container.addChild(new ImageButton("/Interface/hud/laser_small.png", this, "weaponCanon"));

		container = window.addChild(new Container(), 2);
		container.addChild(new ImageButton("/Interface/hud/walrus_small.png", this, "walrus2"));
		container.addChild(new ImageButton("/Interface/hud/missile.png", this, "weaponMissile"));

		container = window.addChild(new Container(), 3);
		container.addChild(new ImageButton("/Interface/hud/walrus_small.png", this, "walrus3"));
		container.addChild(new ImageButton("/Interface/hud/pod.png", this, "weaponPod"));

		container = window.addChild(new Container(), 4);
		container.addChild(new ImageButton("/Interface/hud/walrus_small.png", this, "walrus4"));
		container.addChild(new ImageButton("/Interface/hud/autoPilot.png", this, "autoPilot"));

		container = window.addChild(new Container(), 5);
		container.addChild(new ImageButton("/Interface/hud/centerRudder.png", this, "centerRudder"));
		container.addChild(new ImageButton("/Interface/hud/link.png", this, "link"));

		window.addChild(createInfoBox(), 6);

		window.addChild(createFuelGauge(), 7);
		window.addChild(createThrottleGauge(), 8);

		container = window.addChild(new Container(), 9);
		container.addChild(new ImageButton("/Interface/hud/radar_small.png", this, "radar"));
		container.addChild(new ImageButton("/Interface/hud/rearView.png", this, "rearView"));

		window.addChild(createRadar(), 10);
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		selectWalrus(lastSelectedWalrus);
	}

	private void selectWalrus(int id) {
		setActiveUnit(getState(StatePlayer.class).setActiveUnit(StatePlayer.PlayerUnit.WALRUS, id));
		lastSelectedWalrus = id;
	}

	private void walrus1() {
		selectWalrus(0);
	}

	private void walrus2() {
		selectWalrus(1);
	}

	private void walrus3() {
		selectWalrus(2);
	}

	private void walrus4() {
		selectWalrus(3);
	}

	private void weaponCanon() {
		getPlayerControl().setWeaponType(PlayerControl.WeaponType.CANON);
	}

	private void weaponMissile() {
		getPlayerControl().setWeaponType(PlayerControl.WeaponType.MISSILE);
	}

	private void weaponPod() {
		getPlayerControl().setWeaponType(PlayerControl.WeaponType.POD);
	}

	private void link() {

	}

	private void radar() {

	}
}
