package net.carriercommander.network.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import lombok.*;
import net.carriercommander.network.model.player.GameItemData;

import java.util.List;

/**
 * The message sent from client to server to update the player's status.
 *
 * @author Michael Neuweiler
 */
@Serializable
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor(force = true) // required for serializer
public class MessagePlayerUpdate extends AbstractMessage {
	private final int playerId;
	@Setter
	private List<GameItemData> itemData;
}