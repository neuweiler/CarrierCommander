package net.carriercommander.ui.hud.carrier;

import com.jme3.app.Application;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.style.ElementId;
import net.carriercommander.StatePlayer;
import net.carriercommander.control.PlayerControl;
import net.carriercommander.ui.hud.NavigationState;
import net.carriercommander.ui.hud.widgets.ImageButton;
import net.carriercommander.ui.hud.widgets.PaintedGauge;
import net.carriercommander.ui.hud.widgets.ToggleImageButton;
import net.carriercommander.ui.hud.widgets.Window;

public class StateNavigation extends NavigationState {
	private PaintedGauge depthGauge;
	private QuadBackgroundComponent depthImage;

	@Override
	protected void initialize(Application app) {
		window = new Window(0, 100);

		window.addChild(createRadar(), 1);

		Container container = window.addChild(new Container(), 2);
		container.addChild(new ImageButton("/Interface/hud/carrier/zoomIn.png", this, "zoomIn"));
		container.addChild(new ImageButton("/Interface/hud/carrier/zoomOut.png", this, "zoomOut"));

		window.addChild(new ImageButton("/Interface/hud/carrier/stop.png", this, "stop"), 3);

		container = window.addChild(new Container(), 4);
		container.addChild(new ImageButton("/Interface/hud/shared/centerRudder.png", this, "centerRudder"));
		container.addChild(new ToggleImageButton("/Interface/hud/shared/autoPilot.png", this, "autoPilot"));

		window.addChild(createInfoBox(), 5);

		window.addChild(createDepthGauge(), 6);
		window.addChild(createFuelGauge(), 7);
		window.addChild(createThrottleGauge(), 8);

		container = window.addChild(new Container(), 9);
		container.addChild(new ImageButton("/Interface/hud/carrier/speedIncrease.png", this, "increaseSpeed", new ElementId("repeatButton")));
		container.addChild(new ImageButton("/Interface/hud/carrier/speedDecrease.png", this, "decreaseSpeed", new ElementId("repeatButton")));
	}

	@Override
	protected void cleanup(Application app) {
	}

	private Label createDepthGauge() {
		Label label = new Label("");
		label.setIcon(new IconComponent("/Interface/hud/carrier/depth.png"));
		depthGauge = new PaintedGauge();
		depthImage = new QuadBackgroundComponent();
		label.setBackground(depthImage);
		return label;
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		setActiveUnit(getState(StatePlayer.class).setActiveUnit(StatePlayer.PlayerUnit.CARRIER, 1));
	}

	@Override
	protected void updateGauges() {
		super.updateGauges();
		PlayerControl control = getPlayerControl();

		depthGauge.setValue(0); //TODO get depth
		depthImage.setTexture(depthGauge.getTexture());
	}

	private void increaseSpeed() {
		getPlayerControl().increaseSpeed(0.05f);
	}

	private void decreaseSpeed() {
		getPlayerControl().decreaseSpeed(0.05f);
	}
}
