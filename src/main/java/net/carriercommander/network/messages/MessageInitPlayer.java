package net.carriercommander.network.messages;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * A message to send some initialization data to a new client
 *
 * @author Michael Neuweiler
 */

@Serializable
public class MessageInitPlayer extends AbstractMessage {

	private Vector3f startPosition;

	private MessageInitPlayer() { // required for Serializer
	}

	public MessageInitPlayer(Vector3f startPosition) {
		this.startPosition = startPosition;
	}

	public Vector3f getStartPosition() {
		return startPosition;
	}
}
