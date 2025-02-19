package net.carriercommander.ui.controls.carrier;

import com.jme3.app.Application;
import com.simsilica.lemur.TextField;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.controls.widgets.Window;

public class StateMessages extends WindowState {
	@Override
	protected void initialize(Application app) {
		window = new Window();

		window.addChild(new TextField("Carrier Log"));

		scaleAndPosition(app.getCamera(), 0.5f, 0.5f);
	}


	@Override
	protected void cleanup(Application app) {
	}

}
