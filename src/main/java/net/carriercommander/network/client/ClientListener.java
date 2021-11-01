package net.carriercommander.network.client;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import net.carriercommander.network.messages.ServerStatusMessage;

public class ClientListener implements MessageListener<Client> {

	private final SceneManager sceneManager;

	public ClientListener(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
	}

	@Override
	public void messageReceived(Client source, Message message) {
		if (message instanceof ServerStatusMessage) {
			ServerStatusMessage serverStatusMessage = (ServerStatusMessage) message;
			sceneManager.updatePlayers(serverStatusMessage.getPlayers());
		}
	}

}
