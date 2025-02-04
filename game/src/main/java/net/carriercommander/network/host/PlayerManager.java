package net.carriercommander.network.host;

import com.jme3.network.Server;
import net.carriercommander.network.messages.MessagePlayerAdded;
import net.carriercommander.network.messages.MessagePlayerRemoved;
import net.carriercommander.network.messages.MessagePlayerUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerManager {
	private final Logger logger = LoggerFactory.getLogger(PlayerManager.class);
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
		if (playerId != messagePlayerUpdate.getPlayerId()) {
			logger.error("player {} is trying to send data for player {}", playerId, messagePlayerUpdate.getPlayerId());
			return;
		}

		//TODO add some validations

		server.broadcast(messagePlayerUpdate);
	}

}
