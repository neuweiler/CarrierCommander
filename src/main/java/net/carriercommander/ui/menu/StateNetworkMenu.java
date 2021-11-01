package net.carriercommander.ui.menu;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.*;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.style.ElementId;
import net.carriercommander.Constants;
import net.carriercommander.network.client.StateNetworkClient;
import net.carriercommander.network.host.StateNetworkHost;
import net.carriercommander.ui.WindowState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateNetworkMenu extends WindowState {
	private static final Logger logger = LoggerFactory.getLogger(StateNetworkMenu.class);

	private TextField hostPort, connectPort, connectHost;

	public StateNetworkMenu() {
		setEnabled(false);
	}

	@Override
	protected void initialize(Application app) {
		window = new Container();

		Label title = window.addChild(new Label("Network Game"));
		title.setFontSize(24);
		title.setInsets(new Insets3f(10, 10, 0, 10));

		Container joinPanel = window.addChild(new Container());
		joinPanel.setInsets(new Insets3f(10, 10, 10, 10));
		joinPanel.addChild(new Label("Connect to Game Server", new ElementId("title")));

		Container props = joinPanel.addChild(new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.None, FillMode.Last)));
		props.addChild(new Label("Host address:"));
		connectHost = props.addChild(new TextField(Constants.DEFAULT_HOST), 1);
		props.addChild(new Label("Port:"));
		connectPort = props.addChild(new TextField(String.valueOf(Constants.DEFAULT_PORT)), 1);
		joinPanel.addChild(new ActionButton(new CallMethodAction("Connect", this, "connect")));

		Container hostPanel = window.addChild(new Container());
		hostPanel.setInsets(new Insets3f(10, 10, 10, 10));
		hostPanel.addChild(new Label("Host a Game Server", new ElementId("title")));

		props = hostPanel.addChild(new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.None, FillMode.Last)));
		props.addChild(new Label("Port:"));
		hostPort = props.addChild(new TextField(String.valueOf(Constants.DEFAULT_PORT)), 1);

		hostPanel.addChild(new ActionButton(new CallMethodAction("Begin Hosting", this, "host")));

		ActionButton backButton = window.addChild(new ActionButton(new CallMethodAction("Back", this, "back")));
		backButton.setInsets(new Insets3f(10, 10, 10, 10));

		int height = app.getCamera().getHeight();
		Vector3f pref = window.getPreferredSize().clone();

		float standardScale = getStandardScale();
		pref.multLocal(1.5f * standardScale);

		float y = height * 0.6f + pref.y * 0.5f;

		window.setLocalTranslation(100 * standardScale, y, 0);
		window.setLocalScale(1.5f * standardScale);
	}

	@Override
	protected void cleanup(Application app) {
	}

	protected int validatePort(String title, String sInt ) {
		sInt = sInt.trim();
		if( sInt.length() == 0 ) {
			showError(title + " Error", "Please specify a port.\nDefault is " + Constants.DEFAULT_PORT);
			return -1;
		}
		try {
			return Integer.parseInt(sInt);
		} catch( Exception e ) {
			logger.error("Error parsing port:" + sInt, e);
			showError(title + " Error", "Invalid port:" + sInt + "\n"
					+ e.getClass().getSimpleName() + ":" + e.getMessage());
			return -1;
		}
	}

	protected void showError( String title, String error ) {
		getState(OptionPanelState.class).show(title, error);
	}

	private void connect() {
		logger.info("Connect... host:" + connectHost.getText() + " port:" + connectPort.getText());
		String host = connectHost.getText().trim();
		if( host.length() == 0 ) {
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

	private void host() {
		int port = validatePort("Hosting", hostPort.getText());
		if (port < 0) {
			return;
		}

		try {
			getStateManager().attach(new StateNetworkHost(port));
			setEnabled(false);
		} catch( RuntimeException e ) {
			logger.error("Error attaching host state", e);
			String message = "Error hosting game on port:" + port;
			Throwable cause = e.getCause();
			if( cause != null ) {
				message += "\n" + cause.getClass().getSimpleName() + ":" + cause.getMessage();
			}
			showError("Hosting", message);
		}
	}

	private void back() {
		setEnabled(false);
		getState(StateMainMenu.class).setEnabled(true);
	}
}
