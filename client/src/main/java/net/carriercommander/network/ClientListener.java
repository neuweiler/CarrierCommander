package net.carriercommander.network;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import net.carriercommander.shared.messages.ServerStatusMessage;

public class ClientListener implements MessageListener<Client> {

	private final Client client;
	private final SceneManager sceneManager;

	public ClientListener(Client client, SceneManager sceneManager) {
		this.client = client;
		this.sceneManager = sceneManager;
	}

	@Override
	public void messageReceived(Client source, Message message) {
		if (message instanceof ServerStatusMessage) {
			ServerStatusMessage serverStatusMessage = (ServerStatusMessage) message;
			sceneManager.refreshPlayerList(serverStatusMessage.getPlayers());
		}
	}

}
