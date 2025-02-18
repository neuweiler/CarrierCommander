package net.carriercommander.ui.controls.defense;

import com.jme3.app.Application;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.controls.widgets.Window;

public class StateLaser extends WindowState {
	@Override
	protected void initialize(Application app) {
		window = new Window();
		scaleAndPosition(app.getCamera(), 0.5f, 0);
	}


	@Override
	protected void cleanup(Application app) {
	}

}
