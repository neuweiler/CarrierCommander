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
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for air planes
 *
 * @author Michael Neuweiler
 */
public class PlaneControl extends ShipControl {
	Logger logger = LoggerFactory.getLogger(PlaneControl.class);

	private final Quaternion rotation = new Quaternion();

	protected float attitude = 0; // aka pitch of airplane

	public PlaneControl(CollisionShape shape, float mass) {
		super(shape, mass);
	}

	@Override
	public void prePhysicsTick(PhysicsSpace arg0, float tpf) {
		heading += rudder / 100;
		setLinearVelocity(new Vector3f(FastMath.sin(heading) * throttle * 100, attitude * throttle * -100, FastMath.cos(heading) * throttle * 100));
		setPhysicsRotation(rotation.fromAngles(attitude, heading, -rudder));
	}

	@Override
	public void collision(PhysicsCollisionEvent event) {
		super.collision(event);
		if (event.getObjectA() == this || event.getObjectB() == this) {
			setAttitude(0);
		}
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
}
