package net.carriercommander.ui.hud;

import com.jme3.math.FastMath;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import net.carriercommander.Constants;
import net.carriercommander.control.PlayerControl;
import net.carriercommander.objects.PlayerItem;
import net.carriercommander.terrain.Island;
import net.carriercommander.terrain.IslandMap;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.hud.widgets.PaintedGauge;
import net.carriercommander.ui.hud.widgets.PaintedRadar;

import java.util.Optional;

/**
 * A super class for all states which are involved in navigation of ships/planes.
 * Provides funcionality to add a radar screen and/or fuel and throttle gauges.
 */
public abstract class NavigationState extends WindowState {
	private PaintedRadar radar;
	private PaintedGauge fuelGauge, throttleGauge;
	private Label fuelLabel, throttleLabel, infoBox;
	private QuadBackgroundComponent radarImage, fuelImage, throttleImage;
	private long timestamp = 0;
	private PlayerItem activeUnit;

	@Override
	public void update(float tpf) {
		if (System.currentTimeMillis() > timestamp + 100) {
			updateInfoBox();
			updateRadar();
			updateGauges();
			timestamp = System.currentTimeMillis();
		}
	}

	/**
	 * Retrieve the PlayerControl for the currently active PlayerUnit.
	 *
	 * @return the control for the active PlayerUnit
	 */
	protected PlayerControl getPlayerControl() {
		return activeUnit.getControl(PlayerControl.class);
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

	/**
	 * Update the radar content
	 */
	private void updateRadar() {
		radar.refreshImage();
		radarImage.setTexture(radar.getTexture());
	}

	/**
	 * Updates the gauges created via this class. Note that if more gauges are added in a sub-class,
	 * the responsible class for updating the gauges should overwrite this method and call
	 * super.updateGauges() . The update() implementation of this class will call updateGauges()
	 */
	protected void updateGauges() {
		PlayerControl control = getPlayerControl();

		fuelGauge.setValue(control.getFuel());
		fuelImage.setTexture(fuelGauge.getTexture());

		throttleGauge.setValue(Math.abs(control.getThrottle()));
		throttleImage.setTexture(throttleGauge.getTexture());
	}

	/**
	 * Create a radar widget which displays the surrounding objects
	 *
	 * @return the radar which can be added to a Container
	 */
	protected Label createRadar() {
		Label label = new Label("");
		label.setIcon(new IconComponent("/Interface/hud/radarScreen.png"));
		radar = new PaintedRadar(86, 90, 42, 46, 39, getRootNode());
		radarImage = new QuadBackgroundComponent();
		label.setBackground(radarImage);
		return label;
	}

	/**
	 * Create a text info box which displays the coordinates, heading and other info
	 *
	 * @return the info box which can be added to a Container
	 */
	protected Label createInfoBox() {
		infoBox = new Label("");
		infoBox.setFontSize(12);
		infoBox.setInsets(new Insets3f(5, 5, 5, 5));
//		infoBox.setLocalScale(1.5f * getStandardScale());
//		infoBox.setPreferredSize(infoBox.getPreferredSize().mult(1.5f));
		return infoBox;
	}

	/**
	 * Create a fuel gauge widget
	 *
	 * @return the gauge which can be added to a Container
	 */
	protected Label createFuelGauge() {
		fuelLabel = new Label("");
		fuelLabel.setIcon(new IconComponent("/Interface/hud/fuel.png"));
		fuelGauge = new PaintedGauge();
		fuelImage = new QuadBackgroundComponent();
		fuelLabel.setBackground(fuelImage);
		return fuelLabel;
	}

	/**
	 * Create a throttle gauge widget
	 *
	 * @return the gauge which can be added to a Container
	 */
	protected Label createThrottleGauge() {
		throttleLabel = new Label("");
		throttleLabel.setIcon(new IconComponent("/Interface/hud/speed.png"));
		throttleGauge = new PaintedGauge();
		throttleImage = new QuadBackgroundComponent();
		throttleLabel.setBackground(throttleImage);
		return throttleLabel;
	}

	public PlayerItem getActiveUnit() {
		return activeUnit;
	}

	public void setActiveUnit(PlayerItem activeUnit) {
		this.activeUnit = activeUnit;
		radar.setActiveUnit(activeUnit);
	}

	/** Methods called from the UI widgets (IDE's might indicate that they are never used which is wrong **/

	/**
	 * Zoom in the radar image
	 */
	protected void zoomIn() {
		radar.changeRange(-1000);
	}

	/**
	 * Zoom out of the radar image
	 */
	protected void zoomOut() {
		radar.changeRange(1000);
	}

	/**
	 * Stop the currently active PlayerUnit and center rudder and attitude
	 */
	protected void stop() {
		PlayerControl control = getPlayerControl();
		control.setThrottle(0);
		control.setRudder(0);
		control.setAttitude(0);
	}

	/**
	 * Center the rudder of the currently active PlayerUnit
	 */
	protected void centerRudder() {
		getPlayerControl().setRudder(0);
	}

	/**
	 * Toggle the auto pilot of the currently active PlayerUnit
	 */
	protected void autoPilot() {
		//TODO enable autopilot
	}

	/**
	 * Toggle between fron and read view of the currently active PlayerUnit
	 */
	protected void rearView() {
		activeUnit.toggleRearView();
	}

}
