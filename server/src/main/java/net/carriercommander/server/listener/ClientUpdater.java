package net.carriercommander.server.listener;

import com.jme3.network.Server;
import net.carriercommander.shared.messages.ServerStatusMessage;
import net.carriercommander.shared.model.PlayerData;

import java.util.Map;

public class ClientUpdater implements PlayerManagerListener {

	private final Server server;

	public ClientUpdater(Server server) {
		this.server = server;
	}

	@Override
	public void playerDataChanged(Map<Integer, PlayerData> players) {
		server.broadcast(new ServerStatusMessage(players));
	}
}