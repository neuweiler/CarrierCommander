package net.carriercommander.ui.hud.walrus;

import com.jme3.app.Application;
import net.carriercommander.ui.ControlState;
import net.carriercommander.ui.hud.widgets.ToggleGroup;
import net.carriercommander.ui.hud.widgets.ToggleImageButton;
import net.carriercommander.ui.hud.widgets.Window;

public class StateWalrus extends ControlState {


	@Override
	protected void initialize(Application app) {
		super.initialize(app, StateNavigation.class, StateMap.class,
				StateEquip.class, StateHangar.class, StateStatus.class);

		window = new Window(getApplication().getCamera().getWidth() - 55, getApplication().getCamera().getHeight());

		ToggleGroup group = window.addChild(new ToggleGroup());
		group.addChild(new ToggleImageButton("/Interface/hud/shared/control.png", this, "navigation")).setSelected(true);
		group.addChild(new ToggleImageButton("/Interface/hud/shared/map.png", this, "map"));
		group.addChild(new ToggleImageButton("/Interface/hud/walrus/walrusEquip.png", this, "equip"));
		group.addChild(new ToggleImageButton("/Interface/hud/walrus/walrusHangar.png", this, "hangar"));
		group.addChild(new ToggleImageButton("/Interface/hud/shared/status.png", this, "status"));
	}

	private void navigation() {
		switchToState(StateNavigation.class);
	}

	private void map() {
		switchToState(StateMap.class);
	}

	private void equip() {
		switchToState(StateEquip.class);
	}

	private void hangar() {
		switchToState(StateHangar.class);
	}

	private void status() {
		switchToState(StateStatus.class);
	}

}
