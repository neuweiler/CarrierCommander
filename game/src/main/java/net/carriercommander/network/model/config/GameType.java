package net.carriercommander.network.model.config;

import lombok.Data;

import java.util.List;

@Data
public class GameType {
	private String name;
	private List<PlayerConfig> player;
}
