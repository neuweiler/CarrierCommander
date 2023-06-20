package net.carriercommander.network.model.config;

import lombok.Data;

import java.util.List;

@Data
public class GameConfig {
	private List<Island> islands;
	private List<List<String>> connections;
	private List<GameType> gameTypes;
}
