package net.carriercommander.ui.hud;

import com.jme3.app.Application;
import com.jme3.math.FastMath;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import net.carriercommander.Constants;
import net.carriercommander.StatePlayer;
import net.carriercommander.control.PlayerControl;
import net.carriercommander.objects.PlayerItem;
import net.carriercommander.terrain.Island;
import net.carriercommander.terrain.IslandMap;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.hud.widgets.ImageButton;
import net.carriercommander.ui.hud.widgets.PaintedGauge;
import net.carriercommander.ui.hud.widgets.PaintedRadar;
import net.carriercommander.ui.hud.widgets.Window;

import java.util.Optional;

public class StateCarrierNavigation extends WindowState {

	private PaintedRadar radar;
	private PaintedGauge depthGauge, fuelGauge, throttleGauge;
	private Label radarLabel, depthLabel, fuelLabel, throttleLabel, infoBox;
	private QuadBackgroundComponent radarImage, depthImage, fuelImage, throttleImage;
	private long timestamp = 0;
	private PlayerItem activeUnit;

	@Override
	protected void initialize(Application app) {
		window = new Window(0, 100);

		radarLabel = window.addChild(new Label(""), 1);
		radarLabel.setIcon(new IconComponent("/Interface/hud/radarScreen.png"));
		radar = new PaintedRadar(86, 90, 42, 46, 39, getRootNode());
		radarImage = new QuadBackgroundComponent();
		radarLabel.setBackground(radarImage);

		Container container = window.addChild(new Container(), 2);
		container.addChild(new ImageButton("/Interface/hud/zoomIn.png", this, "zoomIn"));
		container.addChild(new ImageButton("/Interface/hud/zoomOut.png", this, "zoomOut"));

		window.addChild(new ImageButton("/Interface/hud/stop.png", this, "stop"), 3);

		container = window.addChild(new Container(), 4);
		container.addChild(new ImageButton("/Interface/hud/centerRudder.png", this, "centerRudder"));
		container.addChild(new ImageButton("/Interface/hud/autoPilot.png", this, "autoPilot"));

		infoBox = window.addChild(new Label(""), 5);
		infoBox.setFontSize(12);
		infoBox.setInsets(new Insets3f(5, 5, 5, 5));
//		infoBox.setLocalScale(1.5f * getStandardScale());
//		infoBox.setPreferredSize(infoBox.getPreferredSize().mult(1.5f));

		depthLabel = window.addChild(new Label(""), 6);
		depthLabel.setIcon(new IconComponent("/Interface/hud/depth.png"));
		depthGauge = new PaintedGauge(42, 90, 30, 7, 4, 74, true);
		depthImage = new QuadBackgroundComponent();
		depthLabel.setBackground(depthImage);

		fuelLabel = window.addChild(new Label(""), 7);
		fuelLabel.setIcon(new IconComponent("/Interface/hud/fuel.png"));
		fuelGauge = new PaintedGauge(42, 90, 30, 7, 4, 74, true);
		fuelImage = new QuadBackgroundComponent();
		fuelLabel.setBackground(fuelImage);

		throttleLabel = window.addChild(new Label(""), 8);
		throttleLabel.setIcon(new IconComponent("/Interface/hud/speed.png"));
		throttleGauge = new PaintedGauge(42, 90, 30, 7, 4, 74, true);
		throttleImage = new QuadBackgroundComponent();
		throttleLabel.setBackground(throttleImage);

		container = window.addChild(new Container(), 9);
		container.addChild(new ImageButton("/Interface/hud/speedIncrease.png", this, "increaseSpeed"));
		container.addChild(new ImageButton("/Interface/hud/speedDecrease.png", this, "decreaseSpeed"));
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		activeUnit = getState(StatePlayer.class).getActiveUnit();
		radar.setActiveUnit(activeUnit);
	}

	@Override
	public void update(float tpf) {
		if (System.currentTimeMillis() > timestamp + 100) {
			updateInfoBox();
			updateRadar();
			updateGauges();
			timestamp = System.currentTimeMillis();
		}
	}

	private void updateInfoBox() {
		float[] angles = activeUnit.getWorldRotation().toAngles(null);
		Optional<Island> island = IslandMap.getInstance().getClosestIsland(activeUnit.getWorldTranslation());
		infoBox.setText("Position: "
				+ String.format("%.02f", activeUnit.getLocalTranslation().getX() / Constants.MAP_SCENE_FACTOR) + " "
				+ String.format("%.02f", activeUnit.getLocalTranslation().getZ() / Constants.MAP_SCENE_FACTOR) +
				"\nHeading: "
				+ Math.round(FastMath.RAD_TO_DEG * angles[1]) + //TODO not correct heading yet
				"\nIsland: " +
				(island.map(value -> value.getName() +
								" (" + Math.round(value.getPosition().distance(activeUnit.getWorldTranslation()) / Constants.MAP_SCENE_FACTOR) + ")")
						.orElse("-")));
	}

	private void updateRadar() {
		radar.refreshImage();
		radarImage.setTexture(radar.getTexture());
	}

	private void updateGauges() {
		PlayerItem activeUnit = getState(StatePlayer.class).getActiveUnit();
		PlayerControl control = activeUnit.getControl(PlayerControl.class);

		depthGauge.setValue(0); //TODO get depth
		depthImage.setTexture(depthGauge.getTexture());

		fuelGauge.setValue(control.getFuel());
		fuelImage.setTexture(fuelGauge.getTexture());

		throttleGauge.setValue(Math.abs(control.getThrottle()));
		throttleImage.setTexture(throttleGauge.getTexture());
	}


	private void zoomIn() {
		radar.changeRange(-1000);
	}

	private void zoomOut() {
		radar.changeRange(1000);
	}

	private void stop() {
		PlayerControl control = getPlayerControl();
		control.setThrottle(0);
		control.setRudder(0);
		control.setAttitude(0);
	}

	private void centerRudder() {
		getPlayerControl().setRudder(0);
	}

	private void autoPilot() {
		//TODO enable autopilot
	}

	private void increaseSpeed() {
		getPlayerControl().increaseSpeed(0.05f);
	}

	private void decreaseSpeed() {
		getPlayerControl().decreaseSpeed(0.05f);
	}

	private PlayerControl getPlayerControl() {
		return activeUnit.getControl(PlayerControl.class);
	}
}
