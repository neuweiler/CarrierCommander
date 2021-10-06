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
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import jme3utilities.math.MyVector3f;
import net.carriercommander.effects.ExplosionSmall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for missiles
 *
 * @author Michael Neuweiler
 */
public class MissileControl extends ShipControl {
	private static final Logger logger = LoggerFactory.getLogger(MissileControl.class);
	private final Node target;
	private final ExplosionSmall explosion;

	private static final float deltaGainFactor = 0.1f;
	private static final float errorGainFactor = 0.1f;
	private static final Vector3f missileOrientation = new Vector3f(0,0,-1);

	// some re-usable variables to reduce unnecessary allocation/gc
	private static final Vector3f gravity = Vector3f.ZERO;
	private final Vector3f actualDirection = new Vector3f();
	private final Vector3f error = new Vector3f();
	private final Vector3f previousError = new Vector3f();
	private final Vector3f delta = new Vector3f();
	private final Vector3f sum = new Vector3f();
	private final Matrix3f rotationalInertia = new Matrix3f();
	private final Vector3f thrustForce = new Vector3f();

	public MissileControl(CollisionShape shape, Node target, float mass, ExplosionSmall explosion) {
		super(shape, mass);
		this.explosion = explosion;
		this.target = target;
		this.throttle = 1;
	}

	@Override
	public void prePhysicsTick(PhysicsSpace arg0, float tpf) {
		setGravity(gravity);

		Vector3f desiredDirection = calculateDesiredDirection();
		if (calculateError(desiredDirection)) {
			Vector3f impulse = calculateTorqueImpulse();
			applyTorqueImpulse(impulse);
		}

		float currentSpeed = -1 * getLinearVelocity().dot(getPhysicsRotation().getRotationColumn(2));
		float thrust = 150 - currentSpeed;
		if (Math.round(thrust) > 0) {
			getPhysicsRotation().getRotationColumn(2, thrustForce);
			thrustForce.multLocal(thrust * tpf * -10);
			applyForce(thrustForce, rudderOffset);
			fuel -= FastMath.abs(thrust) * 0.0001f * tpf; //TODO find correct factor
		}
	}

	private Vector3f calculateDesiredDirection() {
		getPhysicsRotation().mult(missileOrientation, actualDirection);
		Vector3f desiredDirection = target.getWorldTranslation().subtract(getPhysicsLocation());
		MyVector3f.normalizeLocal(desiredDirection);
		return desiredDirection;
	}

	private boolean calculateError(Vector3f desiredDirection) {
		actualDirection.cross(desiredDirection, error);
		/* Return early if the error angle is 0. Magnify the error if the angle exceeds 90 degrees.*/
		double cosErrorAngle = MyVector3f.dot(actualDirection, desiredDirection);
		float absSinErrorAngle = error.length();
		if (absSinErrorAngle == 0f) {
			if (cosErrorAngle >= 0.0) { // No error at all!
				previousError.set(error);
				return false;
			}
			// Error angle is 180 degrees!
			Vector3f ortho = new Vector3f();
			MyVector3f.generateBasis(desiredDirection, ortho, new Vector3f());
			actualDirection.cross(ortho, error);
			cosErrorAngle = MyVector3f.dot(actualDirection, ortho);
			absSinErrorAngle = error.length();
		}
		Vector3f errorAxis = error.divide(absSinErrorAngle);
		float errorMagnitude = (cosErrorAngle >= 0.0) ? absSinErrorAngle : 1f;
		errorAxis.mult(errorMagnitude, error);

		/* Calculate delta: the change in the error vector.*/
		error.subtract(previousError, delta);
		previousError.set(error);
		return true;
	}

	private Vector3f calculateTorqueImpulse() {
		sum.zero();
		MyVector3f.accumulateScaled(sum, delta, deltaGainFactor); // delta term
		MyVector3f.accumulateScaled(sum, error, errorGainFactor); // proportional term

		/* Scale by the missile's rotational inertia.*/
		getInverseInertiaWorld(rotationalInertia);
		rotationalInertia.invertLocal();
		rotationalInertia.mult(sum, sum);

		return sum;
	}

	@Override
	public void collision(PhysicsCollisionEvent event) {
		if (event.getObjectA() == this || event.getObjectB() == this) {
			if (explosion != null && getSpatial().getParent() != null) {
				explosion.play(getSpatial().getLocalTranslation());
			}
			removeMissile();
		}
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		if (fuel < 0) {
			removeMissile();
		}
	}

	private void removeMissile() {
		if (getPhysicsSpace() != null) {
			getPhysicsSpace().removeCollisionListener(this);
			getPhysicsSpace().removeTickListener(this);
			getPhysicsSpace().remove(this);
		}
		if (getSpatial() != null) {
			getSpatial().removeFromParent();
//			getSpatial().removeControl(this);
		}
	}

}
