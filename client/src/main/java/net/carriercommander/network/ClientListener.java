package net.carriercommander.network;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;

import net.carriercommander.shared.messages.ServerStatusMessage;
import net.carriercommander.shared.messages.TextMessage;

public class ClientListener implements MessageListener<Client> {

	private Client client;
	private SceneManager sceneManager;

	public ClientListener(Client client, SceneManager sceneManager) {
		this.client = client;
		this.sceneManager = sceneManager;
	}

	@Override
	public void messageReceived(Client source, Message message) {
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getMessage();
			System.out.println("message received: " + text);
		} else if (message instanceof ServerStatusMessage) {
			ServerStatusMessage serverStatusMessage = (ServerStatusMessage) message;
			sceneManager.refreshPlayerList(serverStatusMessage.getPlayers());
		}
	}

}
