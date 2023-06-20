package net.carriercommander.network.host;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jme3.network.Server;
import lombok.Getter;
import net.carriercommander.network.model.config.GameConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class IslandManager {
	private final Logger logger = LoggerFactory.getLogger(PlayerManager.class);
	private final Server server;
	@Getter
	private GameConfig gameConfig;

	public IslandManager(Server server) {
		this.server = server;
	}

	public void initialize(String fileName) {
		ObjectMapper mapper = new ObjectMapper();
		File file = new File(fileName);
		try {
			gameConfig = mapper.readValue(file, GameConfig.class);
			logger.info("successfully loaded config from {}", fileName);
		} catch (IOException e) {
			logger.error("unable to load island config from {}", fileName, e);
		}

	}
/*
	public void updateIsland(String name, MessagePlayerUpdate messagePlayerUpdate) {
		server.broadcast(messagePlayerUpdate);
	}
 */
/*
	public Optional<Island> getClosestIsland(Vector3f position) {
		Comparator<Island> minComparator = (n1, n2) ->
				Float.compare(n1.getPosition().distance(position), n2.getPosition().distance(position));
		return islands.stream().min(minComparator);
	}
*/
}
