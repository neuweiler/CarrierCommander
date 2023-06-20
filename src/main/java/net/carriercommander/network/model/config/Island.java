package net.carriercommander.network.model.config;

import com.jme3.network.serializing.Serializable;
import lombok.Data;

@Data
@Serializable
public class Island {
	private String name;
	private int x, y, z;
	private float area;
	private int model;
}
