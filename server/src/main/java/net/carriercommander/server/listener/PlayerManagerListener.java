package net.carriercommander.server.listener;

import net.carriercommander.shared.model.PlayerData;

import java.util.Map;

public interface PlayerManagerListener {
	void playerDataChanged(Map<Integer, PlayerData> players);
}
