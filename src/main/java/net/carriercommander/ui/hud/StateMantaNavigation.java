package net.carriercommander.ui.hud;

import com.jme3.app.Application;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.component.IconComponent;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.hud.widgets.ImageButton;
import net.carriercommander.ui.hud.widgets.Window;

public class StateMantaNavigation extends WindowState {
	@Override
	protected void initialize(Application app) {
		window = new Window(0, 100);

		Container container = window.addChild(new Container(), 1);
		container.addChild(new ImageButton("/Interface/hud/manta.png", this, "manta1"));
		container.addChild(new ImageButton("/Interface/hud/laser.png", this, "weaponCanon"));

		container = window.addChild(new Container(), 2);
		container.addChild(new ImageButton("/Interface/hud/manta.png", this, "manta2"));
		container.addChild(new ImageButton("/Interface/hud/missile.png", this, "weaponMissile"));

		container = window.addChild(new Container(), 3);
		container.addChild(new ImageButton("/Interface/hud/manta.png", this, "manta3"));
		container.addChild(new ImageButton("/Interface/hud/pod.png", this, "weaponBomb"));

		container = window.addChild(new Container(), 4);
		container.addChild(new ImageButton("/Interface/hud/manta.png", this, "manta4"));
		container.addChild(new ImageButton("/Interface/hud/autoPilot.png", this, "autoPilot"));

		container = window.addChild(new Container(), 5);
		container.addChild(new ImageButton("/Interface/hud/centerRudder.png", this, "centerRudder"));
		container.addChild(new ImageButton("/Interface/hud/level.png", this, "levelOff"));

		container = window.addChild(new Container(), 6);
		/*
					<panel childLayout="vertical" style="infoBox">
						<text text="" id="walrusPosition" style="infoText" />
						<text text="" id="walrusHeading" style="infoText" />
						<text text="" id="walrusIsland" style="infoText" />
					</panel>
		*/

		window.addChild(new Button(""), 7).setIcon(new IconComponent("/Interface/hud/altitude.png"));
		window.addChild(new Button(""), 8).setIcon(new IconComponent("/Interface/hud/fuel.png"));
		window.addChild(new Button(""), 9).setIcon(new IconComponent("/Interface/hud/speed.png"));

		container = window.addChild(new Container(), 10);
		container.addChild(new ImageButton("/Interface/hud/radar.png", this, "radar"));
		container.addChild(new ImageButton("/Interface/hud/rearView.png", this, "rearView"));
	}

	@Override
	protected void cleanup(Application app) {
	}

	private void manta1() {

	}

	private void manta2() {

	}

	private void manta3() {

	}

	private void manta4() {

	}

	private void weaponCanon() {

	}

	private void weaponMissile() {

	}

	private void weaponBomb() {

	}

	private void autoPilot() {

	}

	private void centerRudder() {

	}

	private void levelOff() {

	}

	private void radar() {

	}

	private void rearView() {

	}
}
