package net.carriercommander.objects;

import com.jme3.math.Vector3f;
import net.carriercommander.Constants;

public class Island {
	enum IslandType { free, factory, resource, defense };

	private final String name;
	private final Vector3f position;
	private final IslandType type;
	private final float area; // in square km

	Island(String name, Vector3f position, float area, IslandType type) {
		this.name = name;
		this.position = position.mult(Constants.MAP_SCENE_FACTOR);
		this.area = area;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public Vector3f getPosition() {
		return position;
	}

	public IslandType getType() {
		return type;
	}

	public float getArea() {
		return area;
	}
}
