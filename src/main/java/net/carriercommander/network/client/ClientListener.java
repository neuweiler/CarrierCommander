package net.carriercommander.network.client;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import net.carriercommander.network.messages.MessagePlayerAdded;
import net.carriercommander.network.messages.MessagePlayerRemoved;
import net.carriercommander.network.messages.MessagePlayerUpdate;

public class ClientListener implements MessageListener<Client> {

	private final SceneManager sceneManager;

	public ClientListener(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
	}

	@Override
	public void messageReceived(Client source, Message message) {
		if (message instanceof MessagePlayerUpdate) {
			MessagePlayerUpdate messagePlayerUpdate = (MessagePlayerUpdate) message;
			sceneManager.updatePlayer(messagePlayerUpdate.getPlayerId(), messagePlayerUpdate.getItemData());
			return;
		}

		if (message instanceof MessagePlayerAdded) {
			sceneManager.addPlayer(((MessagePlayerAdded) message).getId());
			return;
		}

		if (message instanceof MessagePlayerRemoved) {
			sceneManager.removePlayer(((MessagePlayerRemoved) message).getId());
		}
	}

}
