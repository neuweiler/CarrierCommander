package net.carriercommander.ui.controls.defense;

import com.jme3.app.Application;
import com.simsilica.lemur.Button;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.controls.widgets.Window;

public class StateMissile extends WindowState {
	@Override
	protected void initialize(Application app) {
		window = new Window();

		window.addChild(new Button("missile placeholder")); // just a dummy entry

		scaleAndPosition(app.getCamera(), 0.5f, 0);
	}


	@Override
	protected void cleanup(Application app) {
	}

}
