package net.carriercommander.ui.hud.walrus;

import com.jme3.app.Application;
import com.simsilica.lemur.Container;
import net.carriercommander.StatePlayer;
import net.carriercommander.control.PlayerControl;
import net.carriercommander.ui.hud.NavigationState;
import net.carriercommander.ui.hud.widgets.ImageButton;
import net.carriercommander.ui.hud.widgets.ToggleGroup;
import net.carriercommander.ui.hud.widgets.ToggleImageButton;
import net.carriercommander.ui.hud.widgets.Window;

public class StateNavigation extends NavigationState {
	private int lastSelectedWalrus = 1;
	private ToggleImageButton buttonCanon, buttonMissile, buttonPod, buttonAutoPilot, buttonRearView,
			buttonLink, buttonRadar;

	@Override
	protected void initialize(Application app) {
		window = new Window();

		Container base = window.addChild(new Container(), 1);
		Container container = base.addChild(new Container());
		ToggleGroup group = container.addChild(new ToggleGroup(), 0);
		group.addChild(new ToggleImageButton("/Interface/hud/walrus/walrus_small.png", this, "walrus1"), 0).setSelected(true);
		group.addChild(new ToggleImageButton("/Interface/hud/walrus/walrus_small.png", this, "walrus2"), 1);
		group.addChild(new ToggleImageButton("/Interface/hud/walrus/walrus_small.png", this, "walrus3"), 2);
		group.addChild(new ToggleImageButton("/Interface/hud/walrus/walrus_small.png", this, "walrus4"), 3);
		container.addChild(new ImageButton("/Interface/hud/shared/centerRudder.png", this, "centerRudder"), 1);

		container = base.addChild(new Container());
		group = container.addChild(new ToggleGroup(), 0);
		buttonCanon = group.addChild(new ToggleImageButton("/Interface/hud/shared/laser_small.png", this, "weaponCanon"), 0);
		buttonMissile = group.addChild(new ToggleImageButton("/Interface/hud/shared/missile.png", this, "weaponMissile"), 1);
		buttonPod = group.addChild(new ToggleImageButton("/Interface/hud/shared/pod.png", this, "weaponPod"), 2);
		buttonAutoPilot = container.addChild(new ToggleImageButton("/Interface/hud/shared/autoPilot.png", this, "autoPilot"), 1);
		buttonLink = container.addChild(new ToggleImageButton("/Interface/hud/walrus/link.png", this, "link"), 2);

		window.addChild(createInfoBox(), 6);

		window.addChild(createFuelGauge(), 7);
		window.addChild(createThrottleGauge(), 8);

		container = window.addChild(new Container(), 9);
		buttonRadar = container.addChild(new ToggleImageButton("/Interface/hud/shared/radar_small.png", this, "radar"));
		buttonRearView = container.addChild(new ToggleImageButton("/Interface/hud/shared/rearView.png", this, "rearView"));

		window.addChild(createRadar(), 10);

		scaleAndPosition(app.getCamera(), 0.5f, 0);
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
		buttonRearView.setSelected(getActiveUnit().isRearView());
		buttonLink.setSelected(false); //TODO
		buttonRadar.setSelected(true); // TODO
	}

	private void walrus1() {
		selectWalrus(1);
	}

	private void walrus2() {
		selectWalrus(2);
	}

	private void walrus3() {
		selectWalrus(3);
	}

	private void walrus4() {
		selectWalrus(4);
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
