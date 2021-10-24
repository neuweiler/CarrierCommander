package net.carriercommander.ui.hud;

import com.jme3.app.Application;
import net.carriercommander.ui.ControlState;
import net.carriercommander.ui.hud.widgets.ImageButton;
import net.carriercommander.ui.hud.widgets.ToggleGroup;
import net.carriercommander.ui.hud.widgets.ToggleImageButton;
import net.carriercommander.ui.hud.widgets.Window;

public class StateManta extends ControlState {


	@Override
	protected void initialize(Application app) {
		super.initialize(app, StateMantaNavigation.class, StateMantaMap.class,
				StateMantaEquip.class, StateMantaHangar.class, StateMantaStatus.class);

		window = new Window(getApplication().getCamera().getWidth() - 55, getApplication().getCamera().getHeight());

		ToggleGroup group = window.addChild(new ToggleGroup());
		group.addChild(new ToggleImageButton("/Interface/hud/control.png", this, "navigation")).setSelected(true);
		group.addChild(new ToggleImageButton("/Interface/hud/map.png", this, "map"));
		group.addChild(new ToggleImageButton("/Interface/hud/mantaEquip.png", this, "equip"));
		group.addChild(new ToggleImageButton("/Interface/hud/mantaHangar.png", this, "hangar"));
		group.addChild(new ToggleImageButton("/Interface/hud/status.png", this, "status"));
	}

	private void navigation() {
		switchToState(StateMantaNavigation.class);
	}

	private void map() {
		switchToState(StateMantaMap.class);
	}

	private void equip() {
		switchToState(StateMantaEquip.class);
	}

	private void hangar() {
		switchToState(StateMantaHangar.class);
	}

	private void status() {
		switchToState(StateMantaStatus.class);
	}

}