package net.carriercommander.network.host;

import com.jme3.network.Server;
import net.carriercommander.network.messages.ServerStatusMessage;
import net.carriercommander.network.model.PlayerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ClientUpdater implements PlayerManagerListener {
	private final Logger logger = LoggerFactory.getLogger(ClientUpdater.class);
	private final Server server;

	public ClientUpdater(Server server) {
		this.server = server;
	}

	@Override
	public void playerDataChanged(Map<Integer, PlayerData> players) {
		server.broadcast(new ServerStatusMessage(players));
		if (logger.isDebugEnabled()) {
			logger.debug("sending player data: {}", players.get(0).getCarrier().getLocation());
		}
	}
}
