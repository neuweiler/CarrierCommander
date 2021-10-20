package net.carriercommander.network.client;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.network.*;
import net.carriercommander.Constants;
import net.carriercommander.network.Utils;
import net.carriercommander.network.host.ClientUpdater;
import net.carriercommander.network.host.PlayerDataListener;
import net.carriercommander.network.host.PlayerManager;
import net.carriercommander.network.messages.PlayerDataMessage;
import net.carriercommander.network.messages.TextMessage;
import net.carriercommander.network.model.PlayerData;
import net.carriercommander.ui.AbstractState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StateNetworkClient extends AbstractState implements ClientStateListener {
	private final Logger logger = LoggerFactory.getLogger(StateNetworkClient.class);
	private Client networkClient;
	private final PlayerData playerData = new PlayerData();
	private SceneManager sceneManager;

	@Override
	protected void initialize(Application app) {
		sceneManager = new SceneManager((SimpleApplication) getApplication());
	}

	@Override
	protected void cleanup(Application app) {

	}

	@Override
	protected void onEnable() {
		getApplication().setPauseOnLostFocus(false);

		try {
			logger.info("connecting to {}, port {}", Constants.DEFAULT_HOST, Constants.DEFAULT_PORT);
			networkClient = Network.connectToServer(Constants.DEFAULT_HOST, Constants.DEFAULT_PORT);
			logger.info("connected !");
			networkClient.addMessageListener(new ClientListener(networkClient, sceneManager));
			networkClient.addClientStateListener(this);
			networkClient.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onDisable() {
		getApplication().setPauseOnLostFocus(true);

		if (networkClient != null) {
			networkClient.close();
			networkClient = null;
		}
	}

	@Override
	public void update(float tpf) {
		if (networkClient != null && networkClient.isConnected() && playerData.isModified()) {
			networkClient.send(new PlayerDataMessage(playerData));
			playerData.clean();
		}
	}

	@Override
	public void clientConnected(Client c) {
		playerData.setId(c.getId());
		sceneManager.setMyId(c.getId());
		networkClient.send(new TextMessage("Hello Server! I'm ID" + c.getId()));
	}

	@Override
	public void clientDisconnected(Client arg0, DisconnectInfo arg1) {
		logger.warn("client disconnected!");
	}

}
