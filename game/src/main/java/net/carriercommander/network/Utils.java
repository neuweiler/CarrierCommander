package net.carriercommander.network;

import com.jme3.network.serializing.Serializer;
import net.carriercommander.network.messages.*;
import net.carriercommander.network.model.config.Island;
import net.carriercommander.network.model.player.GameItemData;

public class Utils {

	public static void initSerializers() {
		Serializer.registerClasses(
				MessagePlayerAdded.class,
				MessagePlayerRemoved.class,
				MessagePlayerUpdate.class,
				MessageInitPlayer.class,
				GameItemData.class,
				Island.class
		);
	}
}
