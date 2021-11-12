package net.carriercommander.network;

import com.jme3.network.serializing.Serializer;
import net.carriercommander.network.messages.MessagePlayerAdded;
import net.carriercommander.network.messages.MessagePlayerRemoved;
import net.carriercommander.network.messages.MessagePlayerUpdate;
import net.carriercommander.network.messages.TextMessage;
import net.carriercommander.network.model.GameItemData;

public class Utils {

	public static void initSerializers() {
		Serializer.registerClasses(
				MessagePlayerAdded.class,
				MessagePlayerRemoved.class,
				MessagePlayerUpdate.class,
				TextMessage.class,
				GameItemData.class
		);
	}
}
