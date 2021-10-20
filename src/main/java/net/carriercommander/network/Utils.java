package net.carriercommander.network;

import com.jme3.network.serializing.Serializer;
import net.carriercommander.network.messages.PlayerDataMessage;
import net.carriercommander.network.messages.ServerStatusMessage;
import net.carriercommander.network.messages.TextMessage;
import net.carriercommander.network.model.CarrierData;
import net.carriercommander.network.model.MantaData;
import net.carriercommander.network.model.PlayerData;
import net.carriercommander.network.model.WalrusData;

public class Utils {

	public static void initSerializers() {
		Serializer.registerClasses(
				ServerStatusMessage.class,
				TextMessage.class,
				PlayerDataMessage.class,
				PlayerData.class,
				CarrierData.class,
				MantaData.class,
				WalrusData.class
		);
	}
}
