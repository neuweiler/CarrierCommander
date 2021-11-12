package net.carriercommander.network.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * A message to inform clients about a new player who joined the game
 *
 * @author Michael Neuweiler
 */

@Serializable
public class MessagePlayerAdded extends AbstractMessage {

	private int id;

	private MessagePlayerAdded() { // required for Serializer
	}

	public MessagePlayerAdded(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
