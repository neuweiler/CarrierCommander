package net.carriercommander.network.client;

import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import net.carriercommander.network.messages.MessageInitPlayer;
import net.carriercommander.network.messages.MessagePlayerAdded;
import net.carriercommander.network.messages.MessagePlayerRemoved;
import net.carriercommander.network.messages.MessagePlayerUpdate;
import net.carriercommander.network.model.player.PlayerData;

public class ClientListener implements MessageListener<Client> {

	private final SceneManager sceneManager;
	private final PlayerData playerData;

	public ClientListener(SceneManager sceneManager, PlayerData playerData) {
		this.sceneManager = sceneManager;
		this.playerData = playerData;
	}

	@Override
	public void messageReceived(Client source, Message message) {
		if (message instanceof MessagePlayerUpdate messagePlayerUpdate) {
			sceneManager.updatePlayer(messagePlayerUpdate.getPlayerId(), messagePlayerUpdate.getItemData());
			return;
		}

		if (message instanceof MessagePlayerAdded) {
			sceneManager.addPlayer(((MessagePlayerAdded) message).getId());
			source.send(new MessagePlayerUpdate(playerData.getId(), playerData.getAllItems()));
			return;
		}

		if (message instanceof MessagePlayerRemoved) {
			sceneManager.removePlayer(((MessagePlayerRemoved) message).getId());
		}

		if (message instanceof MessageInitPlayer messageInitPlayer) {
			sceneManager.init(messageInitPlayer.getStartPosition(), messageInitPlayer.getIslands(), messageInitPlayer.getConnections());
		}
	}

}
