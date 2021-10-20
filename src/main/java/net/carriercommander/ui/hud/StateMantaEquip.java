package net.carriercommander.ui.hud;

import com.jme3.app.Application;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.hud.widgets.Window;

public class StateMantaEquip extends WindowState {
	@Override
	protected void initialize(Application app) {
		window = new Window(0, 100);
	}


	@Override
	protected void cleanup(Application app) {
	}

}
