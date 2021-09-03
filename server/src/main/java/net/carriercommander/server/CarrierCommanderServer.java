package net.carriercommander.server;

import com.jme3.app.SimpleApplication;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.system.JmeContext;
import net.carriercommander.server.listener.ClientUpdater;
import net.carriercommander.server.listener.PlayerDataListener;
import net.carriercommander.server.status.PlayerManager;
import net.carriercommander.shared.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CarrierCommanderServer extends SimpleApplication implements ConnectionListener {
	private static final int SERVER_PORT = 6000;
	private final Logger logger = LoggerFactory.getLogger(CarrierCommanderServer.class);
	private PlayerManager playerManager;
	private Server server;

	public static void main(String[] args) {
		Utils.initSerializers();
		CarrierCommanderServer app = new CarrierCommanderServer();
		app.start(JmeContext.Type.Headless);
	}

	@Override
	public void simpleInitApp() {
		playerManager = new PlayerManager();
		try {
			server = Network.createServer(SERVER_PORT);
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
	public void connectionAdded(Server server, HostedConnection hostedConnection) {
		logger.info("client connected, id: {}", hostedConnection.getId());
	}

	@Override
	public void connectionRemoved(Server server, HostedConnection hostedConnection) {
		logger.info("client disconnected, id: {}", hostedConnection.getId());
		playerManager.removePlayer(hostedConnection.getId());
	}

	@Override
	public void destroy() {
		if (server != null) {
			server.close();
		}
		super.destroy();
	}
}