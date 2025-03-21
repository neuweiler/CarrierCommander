package net.carriercommander.ui.menu;

import com.jme3.app.Application;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.CallMethodAction;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.OptionPanelState;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.style.ElementId;
import net.carriercommander.Constants;
import net.carriercommander.network.client.StateNetworkClient;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.controls.widgets.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

public class StateNetworkMenu extends WindowState {
	private static final Logger logger = LoggerFactory.getLogger(StateNetworkMenu.class);

	private TextField connectPort, connectHost;

	public StateNetworkMenu() {
		setEnabled(false);
	}

	@Override
	protected void initialize(Application app) {
		window = new Window();

		Container joinPanel = window.addChild(new Container());
		joinPanel.setInsets(new Insets3f(10, 10, 10, 10));
		joinPanel.addChild(new Label("Connect to Game Server", new ElementId("title")));

		Container props = joinPanel.addChild(new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.None, FillMode.Last)));
		props.addChild(new Label("Host address:"));
		connectHost = props.addChild(new TextField(Constants.DEFAULT_HOST), 1);
		props.addChild(new Label("Port:"));
		connectPort = props.addChild(new TextField(String.valueOf(Constants.DEFAULT_PORT)), 1);
		joinPanel.addChild(new ActionButton(new CallMethodAction("Connect", this, "connect")));

		ActionButton backButton = window.addChild(new ActionButton(new CallMethodAction("Back", this, "back")));
		backButton.setInsets(new Insets3f(10, 10, 10, 10));

		scaleAndPosition(app.getCamera(), .2f, .6f, Constants.MENU_MAGNIFICATION);
	}

	@Override
	protected void cleanup(Application app) {
	}

	protected int validatePort(String title, String sInt ) {
		sInt = sInt.trim();
		if(sInt.isEmpty()) {
			showError(title + " Error", "Please specify a port.\nDefault is " + Constants.DEFAULT_PORT);
			return -1;
		}
		try {
			return Integer.parseInt(sInt);
		} catch( Exception e ) {
			logger.error("Error parsing port:{}", sInt, e);
			showError(title + " Error", "Invalid port:" + sInt + "\n"
					+ e.getClass().getSimpleName() + ":" + e.getMessage());
			return -1;
		}
	}

	protected void showError( String title, String error ) {
		getState(OptionPanelState.class).show(title, error);
	}

	private int findPort(int port) {
		try (ServerSocket socket = new ServerSocket(port)) {
			socket.close();
			return port;
		} catch (IOException e) {
			logger.warn("unable to open port {}, trying to find other free port", port);
		}

		try (ServerSocket socket = new ServerSocket(0)) {
			port = socket.getLocalPort();
			socket.close();
			return port;
		} catch (IOException e) {
			logger.error("unable to find free port", e);
		}
		return 0;
	}

	private void connect() {
		logger.info("Connect... host:{} port:{}", connectHost.getText(), connectPort.getText());
		String host = connectHost.getText().trim();
		if(host.isEmpty()) {
			showError("Connect Error", "Please specify a host.");
			return;
		}
		int port = validatePort("Connect", connectPort.getText());
		if (port < 0) {
			return;
		}

		getStateManager().attach(new StateNetworkClient(host, port));
		setEnabled(false);
	}

	private void back() {
		setEnabled(false);
		getState(StateMainMenu.class).setEnabled(true);
	}
}
