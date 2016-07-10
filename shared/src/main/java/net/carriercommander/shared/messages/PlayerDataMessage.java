package net.carriercommander.shared.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

import net.carriercommander.shared.model.PlayerData;

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