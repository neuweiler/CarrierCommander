package net.carriercommander.server.listener;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Server;

import net.carriercommander.server.status.PlayerManager;
import net.carriercommander.shared.messages.PlayerDataMessage;

public class PlayerDataListener implements MessageListener<HostedConnection> {

  private       Server        server;
  private final PlayerManager playerManager;

  public PlayerDataListener(Server server, PlayerManager playerManager) {
    this.server = server;
    this.playerManager = playerManager;
  }

  @Override
  public void messageReceived(HostedConnection hostedConnection, Message message) {
    if (message instanceof PlayerDataMessage) {
      PlayerDataMessage playerDataMessage = (PlayerDataMessage) message;
      playerManager.addPlayer(playerDataMessage.getPlayerData());
    }
  }
}
