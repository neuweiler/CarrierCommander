package net.carriercommander.network.model;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

/**
 * Data objects which hold the necessary data to exchange with server.
 */
@Serializable
public class GameItemData {

	private final ItemType type;

	private final String id;

	private boolean destroy;

	/**
	 * The position of the object in the scene
	 */
	private final Vector3f location = new Vector3f();
	/**
	 * The orientation / rotation of the object
	 */
	private final Quaternion rotation = new Quaternion();
	/**
	 * The velocity of the object
	 */
	private final Vector3f velocity = new Vector3f();

	private GameItemData() { // required for Serializer
		id = null;
		type = ItemType.unknown;
	}

	GameItemData(String id, ItemType type) {
		this.id = id;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public ItemType getType() {
		return type;
	}

	public Vector3f getLocation() {
		return location;
	}

	public boolean setLocation(Vector3f location) {
		return updateVector(location, this.location);
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public boolean setRotation(Quaternion rotation) {
		return updateQuaternion(rotation, this.rotation);
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public boolean setVelocity(Vector3f velocity) {
		return updateVector(velocity, this.velocity);
	}

	public boolean isDestroy() {
		return destroy;
	}

	public void setDestroy(boolean destroy) {
		this.destroy = destroy;
	}

	/**
	 * Update a vector and flag this object as modified if necesssary
	 * The idea is to skip unnecessary checks in Vector3f.equals() and enhance performance.
	 * @param source source vector
	 * @param dest destination vector
	 */
	private boolean updateVector(Vector3f source, Vector3f dest) {
		if (source.x != dest.x || source.y != dest.y || source.z != dest.z) {
			dest.set(source.x, source.y, source.z);
			return true;
		}
		return false;
	}

	/**
	 * Update a quaternion and flag this object as modified if necessary
	 * The idea is to skip unnecessary checks in Quaternion.equals() and enhance performance.
	 * @param source source quaternion
	 * @param dest destination quaternion
	 */
	private boolean updateQuaternion(Quaternion source, Quaternion dest) {
		if (source.getX() != dest.getX() || source.getY() != dest.getY() || source.getZ() != dest.getZ() || source.getW() != dest.getW()) {
			dest.set(source.getX(), source.getY(), source.getZ(), source.getW());
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "{" +
				"id=" + id +
				", type=" + type +
				", location=" + location +
				", rotation=" + rotation +
				", velocity=" + velocity +
				'}';
	}
}
