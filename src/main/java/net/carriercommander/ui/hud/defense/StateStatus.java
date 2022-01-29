package net.carriercommander.ui.hud.defense;

import com.jme3.app.Application;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.hud.widgets.Window;

public class StateStatus extends WindowState {
	@Override
	protected void initialize(Application app) {
		window = new Window(0, 100);
	}


	@Override
	protected void cleanup(Application app) {
	}

}
