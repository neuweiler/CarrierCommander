package net.carriercommander.network.host;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.jme3.network.*;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.CallMethodAction;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.ElementId;
import net.carriercommander.Constants;
import net.carriercommander.network.client.StateNetworkClient;
import net.carriercommander.network.messages.MessageInitPlayer;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.menu.StateNetworkMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StateNetworkHost extends WindowState implements ConnectionListener {
	private final Logger logger = LoggerFactory.getLogger(StateNetworkHost.class);
	private PlayerManager playerManager;
	private Server server;
	private PlayerMessageListener messageListener;

	private final int port;

	public StateNetworkHost(int port) {
		this.port = port;
	}

	@Override
	protected void initialize(Application app) {
		getApplication().setPauseOnLostFocus(false);
		try {
			server = Network.createServer(Constants.GAME_NAME, Constants.GAME_VERSION, port, port);
			server.addConnectionListener(this);

			playerManager = new PlayerManager(server);
			messageListener = new PlayerMessageListener(playerManager);
			server.addMessageListener(messageListener);
			server.start();

			logger.info("Server started...");

			window = new Container();

			window.addChild(new Label("Hosting Control", new ElementId("title")));
			window.addChild(new ActionButton(new CallMethodAction("Join Game", this, "joinGame")));
			window.addChild(new ActionButton(new CallMethodAction("Stop Hosting", this, "stopHosting")));

			int height = app.getCamera().getHeight();
			Vector3f pref = window.getPreferredSize().clone();

			float standardScale = getStandardScale();
			pref.multLocal(1.5f * standardScale);

			float y = height * 0.6f + pref.y * 0.5f;

			window.setLocalTranslation(100 * standardScale, y, 0);
			window.setLocalScale(1.5f * standardScale);

		} catch (IOException e) {
			logger.error("failed to start the server", e);
		}

	}

	@Override
	protected void cleanup(Application app) {
		getApplication().setPauseOnLostFocus(true);
		if (server != null) {
			server.removeConnectionListener(this);
			server.removeMessageListener(messageListener);
			server.close();
			server = null;
		}
	}

	@Override
	public void connectionAdded(Server server, HostedConnection connection) {
		logger.info("client {} connected to host", connection.getId());
		playerManager.addPlayer(connection.getId());

		Vector3f startPosition = new Vector3f(300, 0, 1700 - connection.getId() * 300);
		connection.send(new MessageInitPlayer(startPosition));
	}

	@Override
	public void connectionRemoved(Server server, HostedConnection connection) {
		logger.info("client {} disconnected from host", connection.getId());
		playerManager.removePlayer(connection.getId());
	}

	private void joinGame() {
		getStateManager().attach(new StateNetworkClient("localhost", port));
		setEnabled(false);
	}

	private void stopHosting() {
		getStateManager().getState(StateNetworkMenu.class).setEnabled(true);
		getStateManager().detach(this);
	}

}
