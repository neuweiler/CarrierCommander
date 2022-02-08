package net.carriercommander.ui.hud.carrier;

import com.jme3.app.Application;
import net.carriercommander.ui.ControlState;
import net.carriercommander.ui.hud.widgets.ToggleGroup;
import net.carriercommander.ui.hud.widgets.ToggleImageButton;
import net.carriercommander.ui.hud.widgets.Window;

public class StateCarrier extends ControlState {


	@Override
	protected void initialize(Application app) {
		super.initialize(app, StateNavigation.class, StateMap.class,
				StateRepair.class, StateResources.class, StateMessages.class);

		window = new Window();

		ToggleGroup group = window.addChild(new ToggleGroup());
		group.addChild(new ToggleImageButton("/Interface/hud/shared/control.png", this, "navigation")).setSelected(true);
		group.addChild(new ToggleImageButton("/Interface/hud/shared/map.png", this, "map"));
		group.addChild(new ToggleImageButton("/Interface/hud/carrier/repair.png", this, "repair"));
		group.addChild(new ToggleImageButton("/Interface/hud/carrier/resources.png", this, "resources"));
		group.addChild(new ToggleImageButton("/Interface/hud/carrier/messages.png", this, "messages"));

		scaleAndPosition(app.getCamera(), 1, 1);
	}

	private void navigation() {
		switchToState(StateNavigation.class);
	}

	private void map() {
		switchToState(StateMap.class);
	}

	private void repair() {
		switchToState(StateRepair.class);
	}

	private void resources() {
		switchToState(StateResources.class);
	}

	private void messages() {
		switchToState(StateMessages.class);
	}

}