package net.carriercommander.ui.controls.walrus;

import com.jme3.app.Application;
import com.simsilica.lemur.Button;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.controls.widgets.Window;

public class StateEquip extends WindowState {
	@Override
	protected void initialize(Application app) {
		window = new Window();

		window.addChild(new Button("equip placeholder")); // just a dummy entry

		scaleAndPosition(app.getCamera(), 0.5f, 0.5f);
	}


	@Override
	protected void cleanup(Application app) {
	}

}
