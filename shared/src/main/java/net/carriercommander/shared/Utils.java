package net.carriercommander.shared;

import com.jme3.network.serializing.Serializer;
import net.carriercommander.shared.messages.PlayerDataMessage;
import net.carriercommander.shared.messages.ServerStatusMessage;
import net.carriercommander.shared.messages.TextMessage;
import net.carriercommander.shared.model.*;

public class Utils {

	public static void initSerializers() {
		Serializer.registerClasses(TextMessage.class,
				PlayerDataMessage.class,
				PlayerData.class,
				GameObject.class,
				CarrierData.class,
				MantaData.class,
				WalrusData.class,
				ServerStatusMessage.class);
	}
}