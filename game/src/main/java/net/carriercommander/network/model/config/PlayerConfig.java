package net.carriercommander.network.model.config;

import lombok.Data;

import java.util.List;

@Data
public class PlayerConfig {
	private List<String> islands;
	private int x, z, heading;
}
