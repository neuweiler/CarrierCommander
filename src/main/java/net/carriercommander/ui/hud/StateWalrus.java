package net.carriercommander.ui.hud;

import com.jme3.app.Application;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.CallMethodAction;
import com.simsilica.lemur.component.IconComponent;
import net.carriercommander.ui.ControlState;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.hud.widgets.ImageButton;
import net.carriercommander.ui.hud.widgets.ToggleGroup;
import net.carriercommander.ui.hud.widgets.Window;

public class StateWalrus extends ControlState {


	@Override
	protected void initialize(Application app) {
		super.initialize(app, StateWalrusNavigation.class, StateWalrusMap.class,
				StateWalrusEquip.class, StateWalrusHangar.class, StateWalrusStatus.class);

		window = new Window(getApplication().getCamera().getWidth() - 55, getApplication().getCamera().getHeight());

		ToggleGroup group = window.addChild(new ToggleGroup());
		group.addChild(new ImageButton("/Interface/hud/control.png", this, "navigation"));
		group.addChild(new ImageButton("/Interface/hud/map.png", this, "map"));
		group.addChild(new ImageButton("/Interface/hud/walrusEquip.png", this, "equip"));
		group.addChild(new ImageButton("/Interface/hud/walrusHangar.png", this, "hangar"));
		group.addChild(new ImageButton("/Interface/hud/status.png", this, "status"));
	}

	private void navigation() {
		switchToState(StateWalrusNavigation.class);
	}

	private void map() {
		switchToState(StateWalrusMap.class);
	}

	private void equip() {
		switchToState(StateWalrusEquip.class);
	}

	private void hangar() {
		switchToState(StateWalrusHangar.class);
	}

	private void status() {
		switchToState(StateWalrusStatus.class);
	}

}
