package net.carriercommander.network.host;

import com.jme3.app.Application;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.network.Server;
import net.carriercommander.Constants;
import net.carriercommander.network.Utils;
import net.carriercommander.ui.AbstractState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StateNetworkHost extends AbstractState implements ConnectionListener {
	private final Logger logger = LoggerFactory.getLogger(StateNetworkHost.class);
	private PlayerManager playerManager;
	private Server server;

	@Override
	protected void initialize(Application app) {
		playerManager = new PlayerManager();
		try {
			server = Network.createServer(Constants.DEFAULT_PORT);
			server.addConnectionListener(this);

			ClientUpdater updater = new ClientUpdater(server);
			playerManager.addListener(updater);

			server.addMessageListener(new PlayerDataListener(server, playerManager));
			server.start();

			logger.info("Server started...");
		} catch (IOException e) {
			logger.error("failed to start the server", e);
		}

	}

	@Override
	protected void cleanup(Application app) {

	}

	@Override
	protected void onEnable() {

	}

	@Override
	protected void onDisable() {
		if (server != null) {
			server.close();
		}
	}

	@Override
	public void connectionAdded(Server server, HostedConnection hostedConnection) {
		logger.info("client connected, id: {}", hostedConnection.getId());
	}

	@Override
	public void connectionRemoved(Server server, HostedConnection hostedConnection) {
		logger.info("client disconnected, id: {}", hostedConnection.getId());
		playerManager.removePlayer(hostedConnection.getId());
	}

}
