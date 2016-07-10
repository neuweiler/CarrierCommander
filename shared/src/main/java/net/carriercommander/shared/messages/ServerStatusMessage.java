package net.carriercommander.shared.messages;

import java.util.Map;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

import net.carriercommander.shared.model.PlayerData;

@Serializable
public class ServerStatusMessage extends AbstractMessage {

	private Map<Integer, PlayerData> players;

	public ServerStatusMessage() {
	}

	public ServerStatusMessage(Map<Integer, PlayerData> players) {
		this.players = players;
	}

	public Map<Integer, PlayerData> getPlayers() {
		return players;
	}
}
