package net.carriercommander.network.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import net.carriercommander.network.model.GameItemData;

import java.util.List;

/**
 * The message sent from client to server to update the player's status.
 *
 * @author Michael Neuweiler
 */
@Serializable
public class MessagePlayerUpdate extends AbstractMessage {

	private List<GameItemData> itemData;
	private int playerId;

	private MessagePlayerUpdate() { // required for Serializer
	}

	public MessagePlayerUpdate(int playerId, List<GameItemData> itemData) {
		this.itemData = itemData;
		this.playerId = playerId;
	}

	public void setItemData(List<GameItemData> itemData) {
		this.itemData = itemData;
	}

	public List<GameItemData> getItemData() {
		return itemData;
	}

	public int getPlayerId() {
		return playerId;
	}
}