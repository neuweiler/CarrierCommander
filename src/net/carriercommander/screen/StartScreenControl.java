package net.carriercommander.screen;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import net.carriercommander.CarrierCommander;

public class StartScreenControl extends AbstractAppState implements ScreenController {

	private Nifty nifty;
	private Screen screen;
	private SimpleApplication app = null;

	public StartScreenControl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.app = (SimpleApplication) app;
	}

	public void startGame(String nextScreen, String type) {
		nifty.gotoScreen(nextScreen); // switch to another screen
		System.out.println("game type: " + type);
		((CarrierCommander)app).startGame(type);
		// start the game and do some more stuff...
	}

	public void quitGame() {
		app.stop();
	}

	@Override
	public void update(float tpf) {
		/** jME update loop! */
	}

	@Override
	public void bind(Nifty nifty, Screen screen) {
		this.nifty = nifty;
		this.screen = screen;
	}

	@Override
	public void onEndScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartScreen() {
		// TODO Auto-generated method stub

	}
}
