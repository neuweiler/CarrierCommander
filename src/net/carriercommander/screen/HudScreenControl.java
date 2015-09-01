package net.carriercommander.screen;

import java.util.HashMap;
import java.util.Map;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import net.carriercommander.CarrierCommander;
import net.carriercommander.Constants;
import net.carriercommander.control.ShipControl;
import net.carriercommander.objects.Carrier;
import net.carriercommander.objects.Manta;
import net.carriercommander.objects.Walrus;

/**
 * Hud Screen Control
 * 
 * @author Michael Neuweiler
 */
public class HudScreenControl extends AbstractAppState implements ScreenController {

	private Nifty nifty;
	private Screen screen;
	private CarrierCommander app = null;
	private Map<String, String> currentSelection = new HashMap<>();

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

	@Override
	public void update(float tpf) {
	}

	@Override
	public void bind(Nifty nifty, Screen screen) {
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
		setControl(Constants.MENU_WEAPONS,  Constants.CONTROL_LASER);
		setControl(Constants.MENU_MANTA,  Constants.CONTROL_CONTROL);
		setControl(Constants.MENU_WALRUS,  Constants.CONTROL_CONTROL);
		
		setControl(Constants.MENU_MAIN, "Carrier");
	}

	/**
	 * Change the 2nd and 3rd level menu content and mark the respective
	 * toggle buttons as (in-)active based on arguments from the hud screen.
	 * 
	 * @param menu the container holding the pressed button 
	 * @param selection the button pressed
	 */
	public void setControl(String menu, String selection) {
		if (selection.equals(currentSelection.get(menu))) {
			return;
		}

		// mark the currently selected toggle button as unselected
		Element element = screen.findElementByName(menu + currentSelection.get(menu) + Constants.CONTROL_EXTENSION_BUTTON);
		if (element != null) {
			element.setStyle(Constants.STYLE_UNSELECTED);
		}
		// hide the current control panel (2nd level)
		element = screen.findElementByName(menu + currentSelection.get(menu) + Constants.CONTROL_EXTENSION_CONTROLS);
		if (element != null) {
			element.hide();
		}
		// hide the current sub panel (3rd level)
		element = screen.findElementByName(currentSelection.get(Constants.CONTROL_SUBCONTROLS));
		if (element != null) {
			element.hide();
		}

		// mark the new toggle button as selected
		element = screen.findElementByName(menu + selection + Constants.CONTROL_EXTENSION_BUTTON);
		if (element != null) {
			element.setStyle(Constants.STYLE_SELECTED);
		}
		// find the control panel to display (2nd and 3rd level)
		element = screen.findElementByName(menu + selection + Constants.CONTROL_EXTENSION_CONTROLS);
		if (element != null) {
			element.show();
			// when button is pressed in main panel, find the current selected 2nd level button to show correct 3rd level panel
			String elementId = selection.toLowerCase() + currentSelection.get(selection.toLowerCase()) + Constants.CONTROL_EXTENSION_CONTROLS;
			if ("main".equals(menu)) {
				element = screen.findElementByName(elementId);
				if (element != null) {
					element.show();
					currentSelection.put(Constants.CONTROL_SUBCONTROLS, elementId);
				}
			} else {
				currentSelection.put(Constants.CONTROL_SUBCONTROLS, menu + selection + Constants.CONTROL_EXTENSION_CONTROLS);
			}
		}
		currentSelection.put(menu, selection);

		setCameraLocation();
	}

	/**
	 * Depending on the currently selected sub control, set the camera location to the
	 * appropriate node and set the correct active unit for user interaction.
	 */
	private void setCameraLocation() {
		String subControl = currentSelection.get(Constants.CONTROL_SUBCONTROLS);
		
		if (subControl == null) {
			return;
		}

		System.out.println("subcontrol: " + subControl);
		if (subControl.equals("carrierControlControls")) {
			Carrier carrier = (Carrier) app.getRootNode().getChild(Constants.CARRIER_PLAYER);
			carrier.setCameraToBridge();
			app.setActiveUnit(carrier);
		} else if ("weapons".equals(subControl)) {

		} else if ("walrusControlControls".equals(subControl)) {
			Walrus walrus = (Walrus) app.getRootNode().getChild(Constants.WALRUS_1);
			walrus.setCameraToFront();
			app.setActiveUnit(walrus);
		} else if ("mantaControlControls".equals(subControl)) {
			Manta manta = (Manta) app.getRootNode().getChild(Constants.MANTA_1);
			manta.setCameraToFront();
			app.setActiveUnit(manta);
		} else if ("games".equals(subControl)) {

		}
	}

	public void carrierStop(String what) {
		ShipControl control = app.getRootNode().getChild(Constants.CARRIER_PLAYER).getControl(ShipControl.class);
		if ("all".equals(what)) {
			control.setThrottle(0);
		}
		control.setRudder(0);
	}
}
