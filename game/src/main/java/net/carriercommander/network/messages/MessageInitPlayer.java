package net.carriercommander.network.messages;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.carriercommander.network.model.config.Island;

import java.util.List;

/**
 * A message to send some initialization data to a new client
 *
 * @author Michael Neuweiler
 */

@Serializable
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true) // required for serializer
public class MessageInitPlayer extends AbstractMessage {
	private final Vector3f startPosition;
	private final List<Island> islands;
	private final List<List<String>> connections;
}
