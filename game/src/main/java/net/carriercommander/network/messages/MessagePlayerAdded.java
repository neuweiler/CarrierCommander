package net.carriercommander.network.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * A message to inform clients about a new player who joined the game
 *
 * @author Michael Neuweiler
 */

@Serializable
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true) // required for serializer
public class MessagePlayerAdded extends AbstractMessage {
	private final int id;
}
