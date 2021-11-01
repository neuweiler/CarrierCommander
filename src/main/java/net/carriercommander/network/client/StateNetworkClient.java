package net.carriercommander.network.client;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Network;
import net.carriercommander.Constants;
import net.carriercommander.Player;
import net.carriercommander.StatePlayer;
import net.carriercommander.network.messages.PlayerDataMessage;
import net.carriercommander.network.messages.TextMessage;
import net.carriercommander.network.model.MantaData;
import net.carriercommander.network.model.PlayerData;
import net.carriercommander.network.model.WalrusData;
import net.carriercommander.ui.AbstractState;
import net.carriercommander.ui.menu.StateLoadGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * State to establish a connection and send our player data to the host
 */
public class StateNetworkClient extends AbstractState implements ClientStateListener {
	private final Logger logger = LoggerFactory.getLogger(StateNetworkClient.class);

	private final PlayerData playerData = new PlayerData();
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
		PhysicsRigidBody control = player.getCarrier().getControl();
		playerData.getCarrier().setLocation(control.getPhysicsLocation());
		playerData.getCarrier().setRotation(control.getPhysicsRotation());
		playerData.getCarrier().setVelocity(control.getLinearVelocity());

		for (int i = 0; i < 4; i++) {
			MantaData mantaData = playerData.getManta(i);
			control = player.getManta().get(i).getControl();
			mantaData.setLocation(control.getPhysicsLocation());
			mantaData.setRotation(control.getPhysicsRotation());
			mantaData.setVelocity(control.getLinearVelocity());

			WalrusData walrusData = playerData.getWalrus(i);
			control = player.getWalrus().get(i).getControl();
			walrusData.setLocation(control.getPhysicsLocation());
			walrusData.setRotation(control.getPhysicsRotation());
			walrusData.setVelocity(control.getLinearVelocity());
		}

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

		getStateManager().attach(new StateLoadGame());
	}

	@Override
	public void clientDisconnected(Client arg0, DisconnectInfo arg1) {
		logger.warn("client disconnected!");
		//TODO either re-connect or stop game
	}

}
