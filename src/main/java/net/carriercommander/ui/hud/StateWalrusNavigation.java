package net.carriercommander.ui.hud;

import com.jme3.app.Application;
import com.simsilica.lemur.Container;
import net.carriercommander.StatePlayer;
import net.carriercommander.control.PlayerControl;
import net.carriercommander.ui.hud.widgets.ImageButton;
import net.carriercommander.ui.hud.widgets.ToggleGroup;
import net.carriercommander.ui.hud.widgets.ToggleImageButton;
import net.carriercommander.ui.hud.widgets.Window;

public class StateWalrusNavigation extends NavigationState {
	private int lastSelectedWalrus = 0;
	private ToggleImageButton buttonCanon, buttonMissile, buttonPod, buttonAutoPilot;

	@Override
	protected void initialize(Application app) {
		window = new Window(0, 100);

		Container base = window.addChild(new Container(), 1);
		Container container = base.addChild(new Container());
		ToggleGroup group = container.addChild(new ToggleGroup(), 0);
		group.addChild(new ToggleImageButton("/Interface/hud/walrus_small.png", this, "walrus1"), 0).setSelected(true);
		group.addChild(new ToggleImageButton("/Interface/hud/walrus_small.png", this, "walrus2"), 1);
		group.addChild(new ToggleImageButton("/Interface/hud/walrus_small.png", this, "walrus3"), 2);
		group.addChild(new ToggleImageButton("/Interface/hud/walrus_small.png", this, "walrus4"), 3);
		container.addChild(new ImageButton("/Interface/hud/centerRudder.png", this, "centerRudder"), 1);

		container = base.addChild(new Container());
		group = container.addChild(new ToggleGroup(), 0);
		buttonCanon = group.addChild(new ToggleImageButton("/Interface/hud/laser_small.png", this, "weaponCanon"), 0);
		buttonMissile = group.addChild(new ToggleImageButton("/Interface/hud/missile.png", this, "weaponMissile"), 1);
		buttonPod = group.addChild(new ToggleImageButton("/Interface/hud/pod.png", this, "weaponPod"), 2);
		buttonAutoPilot = container.addChild(new ToggleImageButton("/Interface/hud/autoPilot.png", this, "autoPilot"), 1);
		container.addChild(new ToggleImageButton("/Interface/hud/link.png", this, "link"), 2);

		window.addChild(createInfoBox(), 6);

		window.addChild(createFuelGauge(), 7);
		window.addChild(createThrottleGauge(), 8);

		container = window.addChild(new Container(), 9);
		container.addChild(new ToggleImageButton("/Interface/hud/radar_small.png", this, "radar"));
		container.addChild(new ToggleImageButton("/Interface/hud/rearView.png", this, "rearView"));

		window.addChild(createRadar(), 10);
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		selectWalrus(lastSelectedWalrus);
		addCrossHairs();
	}

	@Override
	protected void onDisable() {
		super.onDisable();
		removeCrossHairs();
	}

	private void selectWalrus(int id) {
		setActiveUnit(getState(StatePlayer.class).setActiveUnit(StatePlayer.PlayerUnit.WALRUS, id));
		lastSelectedWalrus = id;

		PlayerControl.WeaponType selectedWeapon = getPlayerControl().getWeaponType();
		buttonCanon.setSelected(selectedWeapon == PlayerControl.WeaponType.CANON);
		buttonMissile.setSelected(selectedWeapon == PlayerControl.WeaponType.MISSILE);
		buttonPod.setSelected(selectedWeapon == PlayerControl.WeaponType.POD);
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
