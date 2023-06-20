package net.carriercommander.network.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import lombok.*;

/**
 * A message to inform clients about a player who left the game
 *
 * @author Michael Neuweiler
 */

@Serializable
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true) // required for serializer
public class MessagePlayerRemoved extends AbstractMessage {
	private final int id;
}
