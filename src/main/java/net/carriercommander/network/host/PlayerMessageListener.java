package net.carriercommander.network.host;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import net.carriercommander.network.messages.MessagePlayerUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerMessageListener implements MessageListener<HostedConnection> {
	private final Logger logger = LoggerFactory.getLogger(PlayerMessageListener.class);
	private final PlayerManager playerManager;

	public PlayerMessageListener(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	@Override
	public void messageReceived(HostedConnection hostedConnection, Message message) {
		if (message instanceof MessagePlayerUpdate) {
			MessagePlayerUpdate messagePlayerUpdate = (MessagePlayerUpdate) message;
			playerManager.updatePlayer(hostedConnection.getId(), messagePlayerUpdate);
			if (logger.isTraceEnabled()) {
				logger.trace("playerData received from {}: {}", messagePlayerUpdate.getPlayerId(), messagePlayerUpdate.getItemData());
			}
		}
	}
}
