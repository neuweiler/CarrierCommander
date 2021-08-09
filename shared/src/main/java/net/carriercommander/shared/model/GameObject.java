package net.carriercommander.shared.model;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 * Base class for data objects which hold the necessary data to exchange with server.
 */
@Serializable
public class GameObject {

	/**
	 * The position of the object in the playfield
	 */
	private final Vector3f location;
	/**
	 * The orientation / rotation of the object in the game world
	 */
	private final Quaternion rotation;
	/**
	 * The velocity of the object
	 */
	private final Vector3f velocity;
	private boolean modified;

	GameObject() {
		modified = false;
		location = new Vector3f();
		rotation = new Quaternion();
		velocity = new Vector3f();
	}

	boolean isModified() {
		return modified;
	}

	void clean() {
		modified = false;
	}

	public Vector3f getLocation() {
		return location;
	}

	public void setLocation(Vector3f location) {
		if (!location.equals(this.location)) {
			modified = true;
		}
		this.location.set(location);
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public void setRotation(Quaternion rotation) {
		if (!rotation.equals(this.rotation)) {
			modified = true;
		}
		this.rotation.set(rotation);
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		if (!velocity.equals(this.velocity)) {
			modified = true;
		}
		this.velocity.set(velocity);
	}
}
