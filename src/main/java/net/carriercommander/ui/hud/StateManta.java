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

public class StateManta extends ControlState {


	@Override
	protected void initialize(Application app) {
		super.initialize(app, StateMantaNavigation.class, StateMantaMap.class,
				StateMantaEquip.class, StateMantaHangar.class, StateMantaStatus.class);

		window = new Window(getApplication().getCamera().getWidth() - 55, getApplication().getCamera().getHeight());

		ToggleGroup group = window.addChild(new ToggleGroup());
		group.addChild(new ImageButton("/Interface/hud/control.png", this, "navigation"));
		group.addChild(new ImageButton("/Interface/hud/map.png", this, "map"));
		group.addChild(new ImageButton("/Interface/hud/mantaEquip.png", this, "equip"));
		group.addChild(new ImageButton("/Interface/hud/mantaHangar.png", this, "hangar"));
		group.addChild(new ImageButton("/Interface/hud/status.png", this, "status"));
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