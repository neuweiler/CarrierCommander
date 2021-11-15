package net.carriercommander.network.client;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Network;
import net.carriercommander.Constants;
import net.carriercommander.Player;
import net.carriercommander.StatePlayer;
import net.carriercommander.network.messages.MessagePlayerUpdate;
import net.carriercommander.network.messages.TextMessage;
import net.carriercommander.network.model.GameItemData;
import net.carriercommander.network.model.PlayerData;
import net.carriercommander.ui.AbstractState;
import net.carriercommander.ui.menu.StateLoadGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * State to establish a connection and send our player data to the host
 */
public class StateNetworkClient extends AbstractState implements ClientStateListener {
	private final Logger logger = LoggerFactory.getLogger(StateNetworkClient.class);

	private final PlayerData playerData = new PlayerData();
	private MessagePlayerUpdate messagePlayerUpdate;
	private Client networkClient;
	private SceneManager sceneManager;

	private final String host;
	private final int port;

	public StateNetworkClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

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
			logger.info("connecting to {}, port {}", host, port);
			networkClient = Network.connectToServer(Constants.GAME_NAME, Constants.GAME_VERSION, host, port, port);
			logger.info("connected !");
			networkClient.addMessageListener(new ClientListener(sceneManager));
			networkClient.addClientStateListener(this);
			networkClient.start();
		} catch (IOException e) {
			logger.error("unable to connect", e);
		}

	}

	@Override
	protected void onDisable() {
		getApplication().setPauseOnLostFocus(true); //TODO only do this when no server is running locally

		if (networkClient != null) {
			networkClient.close();
			networkClient = null;
		}
	}

	@Override
	public void update(float tpf) {
		StatePlayer statePlayer = getState(StatePlayer.class);
		if (statePlayer == null) {
			return;
		}

		Player player = statePlayer.getPlayer();
		player.getItems().forEach(playerData::update);

		if (networkClient != null && networkClient.isConnected()) {
			List<GameItemData> updatedItems = playerData.getModifiedItems();
			if (updatedItems.size() > 0) {
				messagePlayerUpdate.setItemData(updatedItems);
				networkClient.send(messagePlayerUpdate);
				playerData.clear();
			}
		}
	}

	@Override
	public void clientConnected(Client c) {
		playerData.setId(c.getId());
		messagePlayerUpdate = new MessagePlayerUpdate();
		sceneManager.setMyPlayerId(c.getId());
	}

	@Override
	public void clientDisconnected(Client arg0, DisconnectInfo arg1) {
		logger.warn("client disconnected!");
		//TODO either re-connect or stop game
		messagePlayerUpdate = null;
	}

}
