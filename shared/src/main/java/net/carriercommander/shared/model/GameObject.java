package net.carriercommander.shared.model;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

@Serializable
public class GameObject {

	private Vector3f location;
	private Quaternion rotation;
	private Vector3f velocity;
	boolean modified;

	public GameObject() {
		modified = false;
		location = new Vector3f();
		rotation = new Quaternion();
		velocity = new Vector3f();
	}

	public boolean modified() {
		return modified;
	}

	public void clean() {
		modified = false;
	}

	public Vector3f getLocation() {
		return location;
	}

	public void setLocation(Vector3f location) {
		this.location = location;
		modified = true;
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
		modified = true;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
		modified = true;
	}
}
