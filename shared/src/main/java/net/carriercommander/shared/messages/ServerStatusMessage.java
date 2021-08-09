package net.carriercommander.shared.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import net.carriercommander.shared.model.PlayerData;

import java.util.Map;

/**
 * The message sent to the clients to inform them on the status of other players.
 *
 * @author Michael Neuweiler
 */
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
