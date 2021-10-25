package net.carriercommander.ui.hud;

import com.jme3.app.Application;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import net.carriercommander.StatePlayer;
import net.carriercommander.control.PlayerControl;
import net.carriercommander.ui.hud.widgets.*;

public class StateMantaNavigation extends NavigationState {
	private PaintedGauge altitudeGauge;
	private QuadBackgroundComponent altitudeImage;
	private int lastSelectedManta = 0;
	private ToggleImageButton buttonCanon, buttonMissile, buttonBomb, buttonAutoPilot;

	@Override
	protected void initialize(Application app) {
		window = new Window(0, 100);

		Container base = window.addChild(new Container(), 1);
		Container container = base.addChild(new Container());
		ToggleGroup group = container.addChild(new ToggleGroup(), 0);
		group.addChild(new ToggleImageButton("/Interface/hud/manta_small.png", this, "manta1"), 0).setSelected(true);
		group.addChild(new ToggleImageButton("/Interface/hud/manta_small.png", this, "manta2"), 1);
		group.addChild(new ToggleImageButton("/Interface/hud/manta_small.png", this, "manta3"), 2);
		group.addChild(new ToggleImageButton("/Interface/hud/manta_small.png", this, "manta4"), 3);
		container.addChild(new ImageButton("/Interface/hud/centerRudder.png", this, "centerRudder"), 1);

		container = base.addChild(new Container());
		group = container.addChild(new ToggleGroup(), 0);
		buttonCanon = group.addChild(new ToggleImageButton("/Interface/hud/laser_small.png", this, "weaponCanon"), 0);
		buttonMissile = group.addChild(new ToggleImageButton("/Interface/hud/missile.png", this, "weaponMissile"), 1);
		buttonBomb = group.addChild(new ToggleImageButton("/Interface/hud/pod.png", this, "weaponBomb"), 2);
		buttonAutoPilot = container.addChild(new ToggleImageButton("/Interface/hud/autoPilot.png", this, "autoPilot"), 1);
		container.addChild(new ImageButton("/Interface/hud/level.png", this, "levelOff"), 2);
		window.addChild(createInfoBox(), 2);

		window.addChild(createAltitudeGauge(), 3);
		window.addChild(createFuelGauge(), 4);
		window.addChild(createThrottleGauge(), 5);

		container = window.addChild(new Container(), 6);
		container.addChild(new ToggleImageButton("/Interface/hud/radar_small.png", this, "radar"));
		container.addChild(new ToggleImageButton("/Interface/hud/rearView.png", this, "rearView"));

		window.addChild(createRadar(), 7);
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		selectManta(lastSelectedManta);
		addCrossHairs();
	}

	@Override
	protected void onDisable() {
		super.onDisable();
		removeCrossHairs();
	}

	private Label createAltitudeGauge() {
		Label label = new Label("");
		label.setIcon(new IconComponent("/Interface/hud/altitude.png"));
		altitudeGauge = new PaintedGauge();
		altitudeImage = new QuadBackgroundComponent();
		label.setBackground(altitudeImage);
		return label;
	}

	@Override
	protected void updateGauges() {
		super.updateGauges();
		PlayerControl control = getPlayerControl();

		altitudeGauge.setValue(control.getPhysicsLocation().getY() / 1000f);
		altitudeImage.setTexture(altitudeGauge.getTexture());
	}

	private void selectManta(int id) {
		setActiveUnit(getState(StatePlayer.class).setActiveUnit(StatePlayer.PlayerUnit.MANTA, id));
		lastSelectedManta = id;

		PlayerControl.WeaponType selectedWeapon = getPlayerControl().getWeaponType();
		buttonCanon.setSelected(selectedWeapon == PlayerControl.WeaponType.CANON);
		buttonMissile.setSelected(selectedWeapon == PlayerControl.WeaponType.MISSILE);
		buttonBomb.setSelected(selectedWeapon == PlayerControl.WeaponType.BOMB);
	}

	private void manta1() {
		selectManta(0);
	}

	private void manta2() {
		selectManta(1);
	}

	private void manta3() {
		selectManta(2);
	}

	private void manta4() {
		selectManta(3);
	}

	private void weaponCanon() {
		getPlayerControl().setWeaponType(PlayerControl.WeaponType.CANON);
	}

	private void weaponMissile() {
		getPlayerControl().setWeaponType(PlayerControl.WeaponType.MISSILE);
	}

	private void weaponBomb() {
		getPlayerControl().setWeaponType(PlayerControl.WeaponType.BOMB);
	}

	private void levelOff() {
		getPlayerControl().setAttitude(0);
	}

	private void radar() {

	}
}
