package net.carriercommander.server.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.carriercommander.server.listener.PlayerManagerListener;
import net.carriercommander.shared.model.PlayerData;

public class PlayerManager {

  private List<PlayerManagerListener> listeners = new ArrayList<>();
  private Map<Integer, PlayerData>    players   = new HashMap<>();

  public void addPlayer(PlayerData playerData) {
    players.put(playerData.getId(), playerData);
    listeners.forEach(l -> l.playerDataChanged(players));
  }

  public void removePlayer(int id) {
    players.remove(id);
  }

  public void addListener(PlayerManagerListener playerManagerListener) {
    listeners.add(playerManagerListener);
  }

  public Map<Integer, PlayerData> getPlayers() {
    return players;
  }
}
