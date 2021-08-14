package net.carriercommander.screen;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.FastMath;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import net.carriercommander.CarrierCommander;
import net.carriercommander.Constants;
import net.carriercommander.PlayerAppState;
import net.carriercommander.control.PlaneControl;
import net.carriercommander.control.ShipControl;
import net.carriercommander.objects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Hud Screen Control
 *
 * @author Michael Neuweiler
 */
public class HudScreenControl extends AbstractAppState implements ScreenController {
	Logger logger = LoggerFactory.getLogger(HudScreenControl.class);

	private Nifty nifty;
	private Screen screen;
	private CarrierCommander app = null;
	private PlayerAppState playerAppState = null;
	private String subControl = null;

	private final Map<String, String> currentSelection = new HashMap<>();
	private int selectedManta = 1;
	private int selectedWalrus = 1;
	private long timestamp = 0;

	private TextRenderer positionTextRenderer = null;
	private TextRenderer headingTextRenderer = null;
	private TextRenderer islandTextRenderer = null;

	/**
	 * Constructor
	 */
	public HudScreenControl() {
	}

	/**
	 * Initialize the app state
	 */
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app = (CarrierCommander) app;
	}

	private void switchSubControl(String subControl) {
		this.subControl = subControl;
		String type = "";
		switch (subControl) {
			case "carrierControlControls":
				type = "carrier";
				break;
			case "mantaControlControls":
				type = "manta";
				break;
			case "walrusControlControls":
				type = "walrus";
				break;
		}
		Element position = screen.findElementById(type + "Position");
		positionTextRenderer = (position != null ? position.getRenderer(TextRenderer.class) : null);
		Element heading = screen.findElementById(type + "Heading");
		headingTextRenderer = (heading != null ? heading.getRenderer(TextRenderer.class) : null);
		Element island = screen.findElementById(type + "Island");
		islandTextRenderer = (island != null ? island.getRenderer(TextRenderer.class) : null);
	}

	@Override
	public void update(float tpf) {
		if (System.currentTimeMillis() > timestamp + 200) {
			if (playerAppState != null && screen != null) {
				if (subControl != currentSelection.get(Constants.CONTROL_SUBCONTROLS)) {
					switchSubControl(currentSelection.get(Constants.CONTROL_SUBCONTROLS));
				}
				if (subControl == null || positionTextRenderer == null || headingTextRenderer == null || islandTextRenderer == null) {
					return;
				}
				PlayerUnit activeUnit = playerAppState.getActiveUnit();
				positionTextRenderer.setText("Position: "
						+ String.format("%.02f", activeUnit.getLocalTranslation().getX() / Constants.MAP_SCENE_FACTOR) + " "
						+ String.format("%.02f", activeUnit.getLocalTranslation().getZ() / Constants.MAP_SCENE_FACTOR));

				float[] angles = activeUnit.getWorldRotation().toAngles(null);
				headingTextRenderer.setText("Heading: "
						+ Math.round(FastMath.RAD_TO_DEG * angles[1])); //TODO not correct heading yet

				Optional<Island> island = IslandMap.getInstance().getClosestIsland(activeUnit.getWorldTranslation());
				islandTextRenderer.setText("Island: " +
						(island.isPresent() ? island.get().getName() : "-") +
						" (" + Math.round(island.get().getPosition().distance(activeUnit.getWorldTranslation()) / Constants.MAP_SCENE_FACTOR) + ")"
				);
			} else {
				playerAppState = app.getStateManager().getState(PlayerAppState.class); // lazy init
			}
			timestamp = System.currentTimeMillis();
		}
	}

	@Override
	public void bind(@Nonnull Nifty nifty, @Nonnull Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
	}

	@Override
	public void onEndScreen() {
	}

	/**
	 * When the start screen is displayed, pre-set some hud buttons
	 */
	@Override
	public void onStartScreen() {
		setControl(Constants.MENU_CARRIER, Constants.CONTROL_CONTROL);
		setControl(Constants.MENU_WEAPONS, Constants.CONTROL_LASER);
		setControl(Constants.MENU_MANTA, Constants.CONTROL_CONTROL);
		setControl(Constants.MENU_WALRUS, Constants.CONTROL_CONTROL);

		setControl(Constants.MENU_MAIN, "Carrier");
	}

	/**
	 * Change the 2nd and 3rd level menu content and mark the respective toggle buttons as (in-)active based on arguments from the hud screen.
	 *
	 * @param menu      the container holding the pressed button
	 * @param selection the button pressed
	 */
	public void setControl(String menu, String selection) {
		if (selection.equals(currentSelection.get(menu))) {
			return;
		}

		// mark the currently selected toggle button as unselected
		setStyle(menu + currentSelection.get(menu) + Constants.CONTROL_EXTENSION_BUTTON, Constants.STYLE_UNSELECTED);
		// hide the current control panel (2nd level)
		showElement(menu + currentSelection.get(menu) + Constants.CONTROL_EXTENSION_CONTROLS, false);
		// hide the current sub panel (3rd level)
		showElement(currentSelection.get(Constants.CONTROL_SUBCONTROLS), false);

		// mark the new toggle button as selected
		setStyle(menu + selection + Constants.CONTROL_EXTENSION_BUTTON, Constants.STYLE_SELECTED);
		// find the control panel to display (2nd and 3rd level)
		showElement(menu + selection + Constants.CONTROL_EXTENSION_CONTROLS, true);
		// when button is pressed in main panel, find the current selected 2nd level button to show correct 3rd level panel
		String elementId = selection.toLowerCase() + currentSelection.get(selection.toLowerCase()) + Constants.CONTROL_EXTENSION_CONTROLS;
		if ("main".equals(menu)) {
			showElement(elementId, true);
			currentSelection.put(Constants.CONTROL_SUBCONTROLS, elementId);
		} else {
			currentSelection.put(Constants.CONTROL_SUBCONTROLS, menu + selection + Constants.CONTROL_EXTENSION_CONTROLS);
		}
		currentSelection.put(menu, selection);

		setCameraLocation();
	}

	/**
	 * Depending on the currently selected sub control, set the camera location to the appropriate node and set the correct active unit for user
	 * interaction.
	 */
	private void setCameraLocation() {
		String subControl = currentSelection.get(Constants.CONTROL_SUBCONTROLS);

		if (subControl == null) {
			return;
		}
		Carrier carrier = (Carrier) app.getRootNode().getChild(Constants.CARRIER_PLAYER);

		logger.debug("subcontrol: {}", subControl);
		switch (subControl) {
			case "carrierControlControls":
				carrier.setCameraToBridge();
				app.getStateManager().getState(PlayerAppState.class).setActiveUnit(carrier);
				break;
			case "carrierMapControls":
				//TODO show map
				break;
			case "carrierRepairControls":
				//TODO show repair screen
				break;
			case "carrierResourcesControls":
				//TODO show resources screen
				break;
			case "carrierMessagesControls":
				//TODO show messages screen
				break;

			case "weaponsLaserControls":
				carrier.setCameraToLaser();
//        app.getStateManager().getState(PlayerAppState.class).setActiveUnit(carrier);
				break;
			case "weaponsFlaresControls":
				carrier.setCameraToFlareLauncher();
//        app.getStateManager().getState(PlayerAppState.class).setActiveUnit(carrier);
				break;
			case "weaponsMissilesControls":
				carrier.setCameraToSurfaceMissile();
//        app.getStateManager().getState(PlayerAppState.class).setActiveUnit(carrier);
				break;
			case "weaponsDronesControls":
				carrier.setCameraToRear();
//        app.getStateManager().getState(PlayerAppState.class).setActiveUnit(carrier);
				break;

			case "walrusControlControls":
				selectWalrus(String.valueOf(selectedWalrus));
				break;
			case "walrusMapControls":
				//TODO show map screen
				break;
			case "walrusEquipControls":
				//TODO show equip screen
				break;
			case "walrusHangarControls":
				carrier.setCameraToRear();
				break;
			case "walrusStatusControls":
				//TODO show status screen
				break;

			case "mantaControlControls":
				selectManta(String.valueOf(selectedManta));
				break;
			case "mantaMapControls":
				//TODO show map screen
				break;
			case "mantaEquipControls":
				//TODO show equip screen
				break;
			case "mantaHangarControls":
				carrier.setCameraToFlightDeck();
				break;
			case "mantaStatusControls":
				//TODO show status screen
				break;

			case "games":

				break;
		}
	}

	public void carrierStop(String what) {
		ShipControl control = app.getRootNode().getChild(Constants.CARRIER_PLAYER).getControl(ShipControl.class);
		if ("all".equals(what)) {
			control.setThrottle(0);
		}
		control.setRudder(0);
	}

	public void selectWalrus(String id) {
		int walrusId = Integer.parseInt(id);
		setStyle("walrus" + selectedWalrus + Constants.CONTROL_EXTENSION_BUTTON, Constants.STYLE_UNSELECTED);
		setStyle("walrus" + walrusId + Constants.CONTROL_EXTENSION_BUTTON, Constants.STYLE_SELECTED);
		selectedWalrus = walrusId;

		Walrus walrus = (Walrus) app.getRootNode().getChild("walrus-" + id);
		if (walrus != null) {
			walrus.setCameraToFront();
			app.getStateManager().getState(PlayerAppState.class).setActiveUnit(walrus);
		}
	}

	public void walrusStop(String what) {
		ShipControl control = app.getRootNode().getChild("walrus-" + selectedWalrus).getControl(ShipControl.class);
		switch (what) {
			case "all":
				control.setThrottle(0);
			case "rudder":
				control.setRudder(0);
				break;
		}
	}

	public void selectManta(String id) {
		int mantaId = Integer.parseInt(id);
		setStyle("manta" + selectedManta + Constants.CONTROL_EXTENSION_BUTTON, Constants.STYLE_UNSELECTED);
		setStyle("manta" + mantaId + Constants.CONTROL_EXTENSION_BUTTON, Constants.STYLE_SELECTED);
		selectedManta = mantaId;

		Manta manta = (Manta) app.getRootNode().getChild("manta-" + id);
		if (manta != null) {
			manta.setCameraToFront();
			app.getStateManager().getState(PlayerAppState.class).setActiveUnit(manta);
		}
	}

	public void mantaStop(String what) {
		PlaneControl control = app.getRootNode().getChild("manta-" + selectedManta).getControl(PlaneControl.class);
		switch (what) {
			case "all":
				control.setThrottle(0);
			case "rudder":
				control.setRudder(0);
				break;
			case "aileron":
				control.setAttitude(0);
				break;
		}
	}

	private void setStyle(String id, String style) {
		Element element = screen.findElementById(id);
		if (element != null) {
			element.setStyle(style);
		}
	}

	private void showElement(String id, boolean show) {
		Element element = screen.findElementById(id);
		if (element != null) {
			if (show) {
				element.show();
			} else {
				element.hide();
			}
		}
	}
}