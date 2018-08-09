package net.carriercommander.server.listener;

import java.util.Map;

import net.carriercommander.shared.model.PlayerData;

public interface PlayerManagerListener {
  void playerDataChanged(Map<Integer, PlayerData> players);
}
