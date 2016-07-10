package net.carriercommander.server;

import java.io.IOException;

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

public class CarrierCommandServer extends SimpleApplication implements ConnectionListener {

	private Server server;
	private PlayerManager playerManager;

	public static void main(String[] args) {
		Utils.initSerializers();
		CarrierCommandServer app = new CarrierCommandServer();
		app.start(JmeContext.Type.Headless);
	}

	@Override
	public void simpleInitApp() {
		playerManager = new PlayerManager();
		initServer(6000);
	}

	private void initServer(int port) {
		try {
			server = Network.createServer(port);
			server.addConnectionListener(this);

			ClientUpdater updater = new ClientUpdater(server);
			playerManager.addListener(updater);

			server.addMessageListener(new PlayerDataListener(server, playerManager));
			server.start();

			System.out.println("Server started...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connectionAdded(Server server, HostedConnection hostedConnection) {
		System.out.println("client connected, id: " + hostedConnection.getId());
	}

	@Override
	public void connectionRemoved(Server server, HostedConnection hostedConnection) {
		System.out.println("client disconnected, id: " + hostedConnection.getId());
		playerManager.removePlayer(hostedConnection.getId());
	}
}