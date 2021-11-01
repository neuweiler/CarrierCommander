package net.carriercommander.network.host;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import net.carriercommander.network.messages.PlayerDataMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerDataListener implements MessageListener<HostedConnection> {
	private final Logger logger = LoggerFactory.getLogger(PlayerDataListener.class);
	private final PlayerManager playerManager;

	public PlayerDataListener(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@Override
	public void messageReceived(HostedConnection hostedConnection, Message message) {
		if (message instanceof PlayerDataMessage) {
			PlayerDataMessage playerDataMessage = (PlayerDataMessage) message;
			playerManager.addPlayer(playerDataMessage.getPlayerData());
//			logger.debug("playerData received: {}", playerDataMessage.getPlayerData());
		}
	}
}
