package net.carriercommander.network.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import net.carriercommander.network.model.PlayerData;

/**
 * The message sent from client to server to update the player's status.
 *
 * @author Michael Neuweiler
 */
@Serializable
public class PlayerDataMessage extends AbstractMessage {

	private PlayerData playerData;

	public PlayerDataMessage() {
	}

	public PlayerDataMessage(PlayerData playerData) {
		this.playerData = playerData;
	}

	public PlayerData getPlayerData() {
		return playerData;
	}
}