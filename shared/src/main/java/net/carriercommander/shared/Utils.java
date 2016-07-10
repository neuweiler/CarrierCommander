package net.carriercommander.shared;

import com.jme3.network.serializing.Serializer;

import net.carriercommander.shared.messages.PlayerDataMessage;
import net.carriercommander.shared.messages.ServerStatusMessage;
import net.carriercommander.shared.messages.TextMessage;
import net.carriercommander.shared.model.GameObject;
import net.carriercommander.shared.model.PlayerData;

public class Utils {

	public static void initSerializers() {
		Serializer.registerClasses(TextMessage.class,
			PlayerDataMessage.class,
			PlayerData.class,
			GameObject.class,
			ServerStatusMessage.class);
	}
}