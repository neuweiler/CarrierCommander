package net.carriercommander.ui.hud;

import com.jme3.app.Application;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import net.carriercommander.StatePlayer;
import net.carriercommander.control.PlayerControl;
import net.carriercommander.ui.hud.widgets.ImageButton;
import net.carriercommander.ui.hud.widgets.PaintedGauge;
import net.carriercommander.ui.hud.widgets.Window;

public class StateMantaNavigation extends NavigationState {
	private PaintedGauge altitudeGauge;
	private QuadBackgroundComponent altitudeImage;
	private int lastSelectedManta = 0;

	@Override
	protected void initialize(Application app) {
		window = new Window(0, 100);

		Container container = window.addChild(new Container(), 1);
		container.addChild(new ImageButton("/Interface/hud/manta_small.png", this, "manta1"));
		container.addChild(new ImageButton("/Interface/hud/laser_small.png", this, "weaponCanon"));

		container = window.addChild(new Container(), 2);
		container.addChild(new ImageButton("/Interface/hud/manta_small.png", this, "manta2"));
		container.addChild(new ImageButton("/Interface/hud/missile.png", this, "weaponMissile"));

		container = window.addChild(new Container(), 3);
		container.addChild(new ImageButton("/Interface/hud/manta_small.png", this, "manta3"));
		container.addChild(new ImageButton("/Interface/hud/pod.png", this, "weaponBomb"));

		container = window.addChild(new Container(), 4);
		container.addChild(new ImageButton("/Interface/hud/manta_small.png", this, "manta4"));
		container.addChild(new ImageButton("/Interface/hud/autoPilot.png", this, "autoPilot"));

		container = window.addChild(new Container(), 5);
		container.addChild(new ImageButton("/Interface/hud/centerRudder.png", this, "centerRudder"));
		container.addChild(new ImageButton("/Interface/hud/level.png", this, "levelOff"));

		window.addChild(createInfoBox(), 6);

		window.addChild(createAltitudeGauge(), 7);
		window.addChild(createFuelGauge(), 8);
		window.addChild(createThrottleGauge(), 9);

		container = window.addChild(new Container(), 10);
		container.addChild(new ImageButton("/Interface/hud/radar_small.png", this, "radar"));
		container.addChild(new ImageButton("/Interface/hud/rearView.png", this, "rearView"));

		window.addChild(createRadar(), 11);
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
