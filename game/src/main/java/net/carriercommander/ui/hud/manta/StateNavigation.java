package net.carriercommander.ui.hud.manta;

import com.jme3.app.Application;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import net.carriercommander.StatePlayer;
import net.carriercommander.control.PlayerControl;
import net.carriercommander.ui.hud.NavigationState;
import net.carriercommander.ui.hud.widgets.*;

public class StateNavigation extends NavigationState {
	private PaintedGauge altitudeGauge;
	private QuadBackgroundComponent altitudeImage;
	private int lastSelectedManta = 1;
	private ToggleImageButton buttonCanon, buttonMissile, buttonBomb, buttonAutoPilot,
			buttonRearView, buttonRadar;

	@Override
	protected void initialize(Application app) {
		window = new Window();

		Container base = window.addChild(new Container(), 1);
		Container container = base.addChild(new Container());
		ToggleGroup group = container.addChild(new ToggleGroup(), 0);
		group.addChild(new ToggleImageButton("/Interface/hud/manta/manta_small.png", this, "manta1"), 0).setSelected(true);
		group.addChild(new ToggleImageButton("/Interface/hud/manta/manta_small.png", this, "manta2"), 1);
		group.addChild(new ToggleImageButton("/Interface/hud/manta/manta_small.png", this, "manta3"), 2);
		group.addChild(new ToggleImageButton("/Interface/hud/manta/manta_small.png", this, "manta4"), 3);
		container.addChild(new ImageButton("/Interface/hud/shared/centerRudder.png", this, "centerRudder"), 1);

		container = base.addChild(new Container());
		group = container.addChild(new ToggleGroup(), 0);
		buttonCanon = group.addChild(new ToggleImageButton("/Interface/hud/shared/laser_small.png", this, "weaponCanon"), 0);
		buttonMissile = group.addChild(new ToggleImageButton("/Interface/hud/shared/missile.png", this, "weaponMissile"), 1);
		buttonBomb = group.addChild(new ToggleImageButton("/Interface/hud/shared/pod.png", this, "weaponBomb"), 2);
		buttonAutoPilot = container.addChild(new ToggleImageButton("/Interface/hud/shared/autoPilot.png", this, "autoPilot"), 1);
		container.addChild(new ImageButton("/Interface/hud/manta/level.png", this, "levelOff"), 2);
		window.addChild(createInfoBox(), 2);

		window.addChild(createAltitudeGauge(), 3);
		window.addChild(createFuelGauge(), 4);
		window.addChild(createThrottleGauge(), 5);

		container = window.addChild(new Container(), 6);
		buttonRadar = container.addChild(new ToggleImageButton("/Interface/hud/shared/radar_small.png", this, "radar"));
		buttonRearView = container.addChild(new ToggleImageButton("/Interface/hud/shared/rearView.png", this, "rearView"));

		window.addChild(createRadar(), 7);

		scaleAndPosition(app.getCamera(), 0.5f, 0);
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
		label.setIcon(new IconComponent("/Interface/hud/manta/altitude.png"));
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
		buttonRearView.setSelected(getActiveUnit().isRearView());
		buttonRadar.setSelected(true); // TODO
	}

	private void manta1() {
		selectManta(1);
	}

	private void manta2() {
		selectManta(2);
	}

	private void manta3() {
		selectManta(3);
	}

	private void manta4() {
		selectManta(4);
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
