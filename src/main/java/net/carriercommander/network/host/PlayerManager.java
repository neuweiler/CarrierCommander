package net.carriercommander.network.host;

import com.jme3.network.Server;
import net.carriercommander.network.messages.MessagePlayerAdded;
import net.carriercommander.network.messages.MessagePlayerRemoved;
import net.carriercommander.network.messages.MessagePlayerUpdate;

public class PlayerManager {
	private final Server server;

	public PlayerManager(Server server) {
		this.server = server;
	}

	public void addPlayer(int playerId) {
		server.broadcast(new MessagePlayerAdded(playerId));
	}

	public void removePlayer(int playerId) {
		server.broadcast(new MessagePlayerRemoved(playerId));
	}

	public void updatePlayer(int playerId, MessagePlayerUpdate messagePlayerUpdate) {
		messagePlayerUpdate.setPlayerId(playerId); // make sure one player cannot update other player's items

		//TODO add some validations

		server.broadcast(messagePlayerUpdate);
	}

}
