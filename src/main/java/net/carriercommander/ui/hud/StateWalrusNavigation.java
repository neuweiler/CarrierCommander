package net.carriercommander.ui.hud;

import com.jme3.app.Application;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.component.IconComponent;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.hud.widgets.ImageButton;
import net.carriercommander.ui.hud.widgets.Window;

public class StateWalrusNavigation  extends WindowState {
	@Override
	protected void initialize(Application app) {
		window = new Window(0, 100);

		Container container = window.addChild(new Container(), 1);
		container.addChild(new ImageButton("/Interface/hud/walrus.png", this, "walrus1"));
		container.addChild(new ImageButton("/Interface/hud/laser.png", this, "weaponCanon"));

		container = window.addChild(new Container(), 2);
		container.addChild(new ImageButton("/Interface/hud/walrus.png", this, "walrus2"));
		container.addChild(new ImageButton("/Interface/hud/missile.png", this, "weaponMissile"));

		container = window.addChild(new Container(), 3);
		container.addChild(new ImageButton("/Interface/hud/walrus.png", this, "walrus3"));
		container.addChild(new ImageButton("/Interface/hud/pod.png", this, "weaponPod"));

		container = window.addChild(new Container(), 4);
		container.addChild(new ImageButton("/Interface/hud/walrus.png", this, "walrus4"));
		container.addChild(new ImageButton("/Interface/hud/autoPilot.png", this, "autoPilot"));

		container = window.addChild(new Container(), 5);
		container.addChild(new ImageButton("/Interface/hud/centerRudder.png", this, "centerRudder"));
		container.addChild(new ImageButton("/Interface/hud/link.png", this, "link"));

		container = window.addChild(new Container(), 6);
		/*
					<panel childLayout="vertical" style="infoBox">
						<text text="" id="walrusPosition" style="infoText" />
						<text text="" id="walrusHeading" style="infoText" />
						<text text="" id="walrusIsland" style="infoText" />
					</panel>
		*/

		window.addChild(new Button(""), 7).setIcon(new IconComponent("/Interface/hud/fuel.png"));
		window.addChild(new Button(""), 8).setIcon(new IconComponent("/Interface/hud/speed.png"));

		container = window.addChild(new Container(), 9);
		container.addChild(new ImageButton("/Interface/hud/radar.png", this, "radar"));
		container.addChild(new ImageButton("/Interface/hud/rearView.png", this, "rearView"));
	}

	@Override
	protected void cleanup(Application app) {
	}

	private void walrus1() {

	}

	private void walrus2() {

	}

	private void walrus3() {

	}

	private void walrus4() {

	}

	private void weaponCanon() {

	}

	private void weaponMissile() {

	}

	private void weaponPod() {

	}

	private void autoPilot() {

	}

	private void centerRudder() {

	}

	private void link() {

	}

	private void radar() {

	}

	private void rearView() {

	}
}
