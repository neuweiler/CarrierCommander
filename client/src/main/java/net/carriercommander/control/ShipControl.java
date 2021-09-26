/*
 * Copyright (c) 2015, Michael Neuweiler
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of CarrierCommander nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package net.carriercommander.control;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for ships
 *
 * @author Michael Neuweiler
 */
public class ShipControl extends RigidBodyControl implements PhysicsCollisionListener, PhysicsTickListener {
	private final Logger logger = LoggerFactory.getLogger(ShipControl.class);
	protected Matrix3f currentRotation = new Matrix3f();
	protected Vector3f engineForce = new Vector3f();
	protected Vector3f rudderOffset = new Vector3f();
	protected float rudder = 0, throttle = 0, enginePower = 0, rudderPositionZ = 0, heading = 0, fuel = 1.0f;

	public ShipControl(CollisionShape shape, float mass) {
		super(shape, mass);
	}

	@Override
	public void setPhysicsSpace(PhysicsSpace space) {
		super.setPhysicsSpace(space);
		if (space != null) {
			space.addCollisionListener(this);
			space.addTickListener(this);
		}
	}

	@Override
	public void physicsTick(PhysicsSpace arg0, float arg1) {
	}


	@Override
	public void prePhysicsTick(PhysicsSpace arg0, float tpf) {
		enginePower = throttle * getMass() * 10;

		getPhysicsRotationMatrix(currentRotation);
		heading = FastMath.atan2(currentRotation.get(0, 2), currentRotation.get(2, 2));

		engineForce.setX(enginePower * FastMath.sin(heading - rudder / 2));
		engineForce.setZ(-enginePower * FastMath.cos(heading  + FastMath.PI - rudder / 2));

		rudderOffset.setX(-rudderPositionZ * FastMath.sin(heading));
		rudderOffset.setZ(-rudderPositionZ * FastMath.cos(heading));

		if (fuel > 0) {
			fuel -= FastMath.abs(throttle) * 0.00001f * tpf; //TODO find correct factor
			applyForce(engineForce, rudderOffset);
		}
	}

	@Override
	public void collision(PhysicsCollisionEvent event) {
		if (event.getObjectA() == this || event.getObjectB() == this) {
			logger.debug("collision between {} and {}", event.getObjectA(), event.getObjectB());
//			setThrottle(0);
//			setRudder(0);
		}
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

	public void setRudderPositionZ(float rudderPositionZ) {
		this.rudderPositionZ = rudderPositionZ;
	}

	public float getAttitude() {
		return 0f;
	}

	public void setAttitude(float attitude) {
	}
}
