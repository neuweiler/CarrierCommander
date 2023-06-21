package net.carriercommander.ui.menu;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.event.ConsumingMouseListener;
import com.simsilica.lemur.event.MouseEventControl;
import com.simsilica.lemur.style.ElementId;
import net.carriercommander.Constants;
import net.carriercommander.network.client.StateNetworkClient;
import net.carriercommander.network.host.StateNetworkHost;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.hud.widgets.Window;

public class StateMainMenu extends WindowState {
	private ListBox listWorld, listType;

	@Override
	protected void initialize(Application app) {
		window = new Window();

		Container topRow = window.addChild(new Container());
		Label title = topRow.addChild(new Label("Carrier Commander"));
		title.setFontSize(24); // TODO move to styles
		title.setInsets(new Insets3f(10, 10, 0, 10)); // TODO move to styles

		Container middleRow = window.addChild(new Container());
		Container gameSelectionPanel = middleRow.addChild(new Container(), 0);

		Container worldPanel = gameSelectionPanel.addChild(new Container());
		worldPanel.addChild(new Label("Select World", new ElementId("window.title.label")));
		listWorld = worldPanel.addChild(new ListBox());
		listWorld.setVisibleItems(4);
		for( int i = 0; i < 5; i++ ) {
			listWorld.getModel().add("Item " + i);
		}

		Container gamePanel = gameSelectionPanel.addChild(new Container());
		gamePanel.addChild(new Label("Select Game Type", new ElementId("window.title.label")));
		listType = gamePanel.addChild(new ListBox());
		listType.setVisibleItems(3);
		listType.getModel().add("Action");
		listType.getModel().add("Strategy");

		Container gameParamsPanel = middleRow.addChild(new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.None, FillMode.Last)), 1);
		gameParamsPanel.addChild(new Label("Select xyz", new ElementId("window.title.label")));
		gameParamsPanel.addChild(new Label("Human players:"));
		gameParamsPanel.addChild(new TextField("1"), 1);
		gameParamsPanel.addChild(new Label("AI players:"));
		gameParamsPanel.addChild(new TextField("1"), 1);
		gameParamsPanel.addChild(new Label("Teams:"));
		gameParamsPanel.addChild(new TextField("0"), 1);
		gameParamsPanel.addChild(new Label("Port:"));
		gameParamsPanel.addChild(new TextField(String.valueOf(Constants.DEFAULT_PORT)), 1);
		gameParamsPanel.addChild(new Label("Wait for players:"));
		gameParamsPanel.addChild(new TextField("0"), 1);

		Container bottoRom = window.addChild(new Container());
		bottoRom.setInsets(new Insets3f(10, 10, 10, 10)); // TODO move to styles
		bottoRom.addChild(new ActionButton(new CallMethodAction("Start Game", this, "start")), 0);
		bottoRom.addChild(new ActionButton(new CallMethodAction("Exit", app, "stop")), 2);
		bottoRom.addChild(new ActionButton(new CallMethodAction("Connect to Host", this, "connect")), 1);

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
//			action();
		}
	}

	protected void start() {
		startGame(Constants.GameType.action);
	}

	protected void connect() {
		startGame(Constants.GameType.strategy);
	}

	protected void network() {
		getStateManager().getState(StateNetworkMenu.class).setEnabled(true);
		setEnabled(false);
	}

	public void startGame(Constants.GameType gameType) {
		getStateManager().attach(new StateNetworkHost(54321)); //TODO get port from UI element
		getStateManager().attach(new StateNetworkClient("localhost", 54321)); //TODO get port
//		getStateManager().attach(new StateLoadGame(new Vector3f(300, 0, 1700)));
		setEnabled(false);
	}

}
