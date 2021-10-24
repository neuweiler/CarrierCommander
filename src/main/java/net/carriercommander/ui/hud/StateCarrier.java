package net.carriercommander.ui.hud;

import com.jme3.app.Application;
import net.carriercommander.ui.ControlState;
import net.carriercommander.ui.hud.widgets.ToggleGroup;
import net.carriercommander.ui.hud.widgets.ToggleImageButton;
import net.carriercommander.ui.hud.widgets.Window;

public class StateCarrier extends ControlState {


	@Override
	protected void initialize(Application app) {
		super.initialize(app, StateCarrierNavigation.class, StateCarrierMap.class,
				StateCarrierRepair.class, StateCarrierResources.class, StateCarrierMessages.class);

		window = new Window(getApplication().getCamera().getWidth() - 55, getApplication().getCamera().getHeight());

		ToggleGroup group = window.addChild(new ToggleGroup());
		group.addChild(new ToggleImageButton("/Interface/hud/control.png", this, "navigation")).setSelected(true);
		group.addChild(new ToggleImageButton("/Interface/hud/map.png", this, "map"));
		group.addChild(new ToggleImageButton("/Interface/hud/repair.png", this, "repair"));
		group.addChild(new ToggleImageButton("/Interface/hud/resources.png", this, "resources"));
		group.addChild(new ToggleImageButton("/Interface/hud/messages.png", this, "messages"));
	}

	private void navigation() {
		switchToState(StateCarrierNavigation.class);
	}

	private void map() {
		switchToState(StateCarrierMap.class);
	}

	private void repair() {
		switchToState(StateCarrierRepair.class);
	}

	private void resources() {
		switchToState(StateCarrierResources.class);
	}

	private void messages() {
		switchToState(StateCarrierMessages.class);
	}

}