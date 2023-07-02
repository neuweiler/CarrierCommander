package net.carriercommander.control;

import com.jme3.bullet.collision.shapes.CollisionShape;
import net.carriercommander.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerControl extends BaseControl {
	private static final Logger logger = LoggerFactory.getLogger(PlayerControl.class);

	public enum WeaponType {CANON, LASER, MISSILE, BOMB, POD}

	protected float rudder = 0, attitude = 0, throttle = 0, heading = 0, fuel = 1.0f;
	protected WeaponType weaponType = WeaponType.CANON;

	public PlayerControl(CollisionShape shape, float mass) {
		super(shape, mass);
	}

	public void steerLeft(float tpf) {
		setRudder(rudder += Config.sensitivityRudder * tpf);
	}

	public void steerRight(float tpf) {
		setRudder(rudder -= Config.sensitivityRudder * tpf);
	}

	public void steerUp(float tpf) {
		setAttitude(attitude -= Config.sensitivityAttitude * tpf);
	}

	public void steerDown(float tpf) {
		setAttitude(attitude += Config.sensitivityAttitude * tpf);
	}

	public void increaseSpeed(float tpf) {
		setThrottle(throttle += Config.sensitivityThrottle * tpf);
	}

	public void decreaseSpeed(float tpf) {
		setThrottle(throttle -= Config.sensitivityThrottle * tpf);
	}

	public float getAttitude() {
		return attitude;
	}

	public void setAttitude(float attitude) {
		this.attitude = constrain(attitude, -1.0f, 1.0f);
		logger.debug("attitude: {}", this.attitude);
	}

	public float getRudder() {
		return rudder;
	}

	public void setRudder(float rudder) {
		this.rudder = constrain(rudder, -1.0f, 1.0f);
		logger.debug("rudder: {}", this.rudder);
	}

	public float getThrottle() {
		return throttle;
	}

	public void setThrottle(float throttle) {
		this.throttle = constrain(throttle, -0.2f, 1.0f);
		logger.debug("throttle: {}", this.throttle);
	}

	private float constrain(float value, float min, float max) {
		return value > max ? max : value < min ? min : value;
	}

	public float getFuel() {
		return fuel;
	}

	public void setFuel(float fuel) {
		this.fuel = constrain(fuel, 0.0f, 1.0f);
	}

	public WeaponType getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(WeaponType weaponType) {
		this.weaponType = weaponType;
	}
}
