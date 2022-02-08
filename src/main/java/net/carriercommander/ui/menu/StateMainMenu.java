package net.carriercommander.ui.menu;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.*;
import net.carriercommander.Constants;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.hud.widgets.Window;

public class StateMainMenu extends WindowState {

	@Override
	protected void initialize(Application app) {
		window = new Window();

		Label title = window.addChild(new Label("Carrier Commander"));
		title.setFontSize(24); // TODO move to styles
		title.setInsets(new Insets3f(10, 10, 0, 10)); // TODO move to styles

		Container mainPanel = window.addChild(new Container());
		mainPanel.setInsets(new Insets3f(10, 10, 10, 10)); // TODO move to styles
		mainPanel.addChild(new ActionButton(new CallMethodAction("Action Game", this, "action")));
		mainPanel.addChild(new ActionButton(new CallMethodAction("Strategy Game", this, "strategy")));
		mainPanel.addChild(new ActionButton(new CallMethodAction("Network Game", this, "network")));

		ActionButton exit = window.addChild(new ActionButton(new CallMethodAction("Exit Game", app, "stop")));
		exit.setInsets(new Insets3f(10, 10, 10, 10)); // TODO move to styles

		scaleAndPosition(app.getCamera(), .2f, .6f, Constants.MENU_MAGNIFICATION);
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		GuiGlobals.getInstance().requestFocus(window);

		if (Constants.AUTOSTART) {
			action();
		}
	}

	protected void action() {
		startGame(Constants.GameType.action);
	}

	protected void strategy() {
		startGame(Constants.GameType.strategy);
	}

	protected void network() {
		getStateManager().getState(StateNetworkMenu.class).setEnabled(true);
		setEnabled(false);
	}

	public void startGame(Constants.GameType gameType) {
		getStateManager().attach(new StateLoadGame(new Vector3f(300, 0, 1700)));
		setEnabled(false);
	}

}
