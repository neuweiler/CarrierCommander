package net.carriercommander.control;

import com.jme3.bullet.collision.shapes.CollisionShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerControl extends BaseControl {
	private static final Logger logger = LoggerFactory.getLogger(PlayerControl.class);

	protected float rudder = 0, attitude = 0, throttle = 0, heading = 0, fuel = 1.0f;

	public PlayerControl(CollisionShape shape, float mass) {
		super(shape, mass);
	}
	public void steerLeft(float tpf) {
		setRudder(rudder += 0.5f * tpf);
	}

	public void steerRight(float tpf) {
		setRudder(rudder -= 0.5f * tpf);
	}

	public void steerUp(float tpf) {
		setAttitude(attitude -= 0.5f * tpf);
	}

	public void steerDown(float tpf) {
		setAttitude(attitude += 0.5f * tpf);
	}

	public void increaseSpeed(float tpf) {
		setThrottle(throttle += 0.5f * tpf);
	}

	public void decreaseSpeed(float tpf) {
		setThrottle(throttle -= 0.5f * tpf);
	}

	public float getAttitude() {
		return attitude;
	}

	public void setAttitude(float attitude) {
		if (attitude > 1.0f)
			attitude = 1.0f;
		if (attitude < -1.0f)
			attitude = -1.0f;
		this.attitude = attitude;
		logger.debug("attitude: {}", this.attitude);
	}

	public float getRudder() {
		return rudder;
	}

	public void setRudder(float rudder) {
		if (rudder > 1.0f)
			rudder = 1.0f;
		if (rudder < -1.0f)
			rudder = -1.0f;
		this.rudder = rudder;
		logger.debug("rudder: {}", this.rudder);
	}

	public float getThrottle() {
		return throttle;
	}

	public void setThrottle(float throttle) {
		if (throttle > 1.0f)
			throttle = 1.0f;
		if (throttle < -0.2f)
			throttle = -0.2f;
		this.throttle = throttle;
		logger.debug("throttle: {}", this.throttle);
	}

	public float getFuel() {
		return fuel;
	}

	public void setFuel(float fuel) {
		this.fuel = fuel;
	}


}
