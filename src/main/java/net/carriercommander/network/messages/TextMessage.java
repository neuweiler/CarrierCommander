package net.carriercommander.network.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * A message to send generic text messages between client and server.
 *
 * @author Michael Neuweiler
 */
//TODO should be replaced by a more specific message
@Serializable
public class TextMessage extends AbstractMessage {

	private String message;

	private TextMessage() {
	}

	public TextMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
