package net.carriercommander.shared.messages;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class TextMessage extends AbstractMessage {

	private String message;

	public TextMessage() {
	}

	public TextMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
