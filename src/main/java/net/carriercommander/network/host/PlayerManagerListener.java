package net.carriercommander.network.host;

import net.carriercommander.network.model.PlayerData;

import java.util.Map;

public interface PlayerManagerListener {
	void playerDataChanged(Map<Integer, PlayerData> players);
}
