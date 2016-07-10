package net.carriercommander.server.listener;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.Server;

import net.carriercommander.server.status.PlayerManager;
import net.carriercommander.shared.messages.PlayerDataMessage;
import net.carriercommander.shared.messages.TextMessage;

public class PlayerDataListener implements com.jme3.network.MessageListener<HostedConnection> {

	private final PlayerManager playerManager;
	private Server server;

	public PlayerDataListener(Server server, PlayerManager playerManager) {
		this.server = server;
		this.playerManager = playerManager;
	}

	@Override
	public void messageReceived(HostedConnection hostedConnection, Message message) {
		if (message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) message;
//			System.out.println("incoming message: " + textMessage.getMessage());
		} else if (message instanceof PlayerDataMessage) {
			PlayerDataMessage playerDataMessage = (PlayerDataMessage) message;
			playerManager.addPlayer(playerDataMessage.getPlayerData());
//			System.out.println("incoming message: " + playerDataMessage.getPlayerData());
		}
	}
}
