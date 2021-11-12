package net.carriercommander.network.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * A message to inform clients about a player who left the game
 *
 * @author Michael Neuweiler
 */

@Serializable
public class MessagePlayerRemoved extends AbstractMessage {

	private int id;

	private MessagePlayerRemoved() { // required for Serializer
	}

	public MessagePlayerRemoved(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
