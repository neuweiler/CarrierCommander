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
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for missiles
 *
 * @author Michael Neuweiler
 */
public class MissileControl extends ShipControl {
	Logger logger = LoggerFactory.getLogger(MissileControl.class);

	private Node target;

	public MissileControl(CollisionShape shape, Node target, float mass) {
		super(shape, mass);
		this.target = target;
		this.throttle = 1;
	}

	@Override
	public void prePhysicsTick(PhysicsSpace arg0, float tpf) {
		setGravity(Vector3f.ZERO);

		Vector3f delta = target.getWorldTranslation().subtract(getPhysicsLocation());
		Vector3f localDelta = getPhysicsRotation().inverse().mult(delta).normalize();
		Vector3f torque = new Vector3f(localDelta.y, -localDelta.x, 0);
		torque.multLocal(10 * tpf);
		applyTorque(torque);

		Quaternion q = getPhysicsRotation();
		Quaternion t = new Quaternion(q.getX(), q.getY(), 0, q.getW());
		setPhysicsRotation(t);

		float currentSpeed = -1 * getLinearVelocity().dot(getPhysicsRotation().getRotationColumn(2));
		float thrust = 150 - currentSpeed;
		if (Math.round(thrust) > 0) {
			applyForce(new Vector3f(getPhysicsRotation().getRotationColumn(2).mult(-thrust)), rudderOffset);
		}
		if (fuel > 0) {
			fuel -= FastMath.abs(thrust) * 0.001f * tpf; //TODO find correct factor
			logger.info("thrust: {} fuel: {}", thrust, fuel);
		} else {
			removeMissile();
		}
	}

	@Override
	public void collision(PhysicsCollisionEvent event) {
		if (event.getObjectA() == this || event.getObjectB() == this) {
			removeMissile();
		}
	}

	private void removeMissile() {
		if (getPhysicsSpace() != null) {
			getPhysicsSpace().removeTickListener(this);
		}
		getSpatial().removeFromParent();
		getSpatial().removeControl(this);
		getPhysicsSpace().remove(this);
	}

}
