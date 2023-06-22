package net.carriercommander.network.host;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jme3.network.Server;
import lombok.Getter;
import net.carriercommander.network.model.config.GameConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IslandManager {
	private final Logger logger = LoggerFactory.getLogger(PlayerManager.class);

	public IslandManager() {
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
