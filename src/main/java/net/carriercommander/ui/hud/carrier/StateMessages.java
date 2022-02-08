package net.carriercommander.ui.hud.carrier;

import com.jme3.app.Application;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.hud.widgets.Window;

public class StateMessages extends WindowState {
	@Override
	protected void initialize(Application app) {
		window = new Window();
		scaleAndPosition(app.getCamera(), 0.5f, 0.5f);
	}


	@Override
	protected void cleanup(Application app) {
	}

}