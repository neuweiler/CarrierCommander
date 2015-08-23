package net.carriercommander.screen;

import java.util.HashMap;
import java.util.Map;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import net.carriercommander.CarrierCommander;

public class HudScreenControl extends AbstractAppState implements ScreenController {

	private Nifty nifty;
	private Screen screen;
	private SimpleApplication app = null;
	private Map<String, String> currentSelection = new HashMap<>();

	public HudScreenControl() {
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app = (SimpleApplication) app;
	}

	public void setControl(String area, String selection) {
		if (selection.equals(currentSelection.get(area))) {
			return;
		}
		
		Element element = screen.findElementByName(area + currentSelection.get(area) + "Button");
		if (element != null) {
			element.setStyle("unselected");
		}
		element = screen.findElementByName(area + currentSelection.get(area) + "Controls");
		if (element != null) {
			element.hide();
		}

		element = screen.findElementByName(area + selection + "Button");
		if (element != null) {
			element.setStyle("selected");
		}
		element = screen.findElementByName(area + selection + "Controls");
		if (element != null) {
			element.show();
		}
		currentSelection.put(area, selection);
	}

	@Override
	public void update(float tpf) {
		/** jME update loop! */
	}

	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartScreen() {
		setControl("carrier", "Control");
		setControl("weapons", "Laser");
		setControl("manta", "Control");
		setControl("walrus", "Control");
		setControl("main", "Carrier");
	}
}
