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
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import net.carriercommander.effects.ImpactProjectile;

/**
 * Controller for projectiles
 *
 * @author Michael Neuweiler
 */
public class ProjectileControl extends PlayerControl {
	private static final int INITIAL_SPEED = 100;
	private static final float FUEL_CONSUMPTION_PER_CYCLE = 0.1f;

	private static final Vector3f gravity = Vector3f.ZERO;
	private final Vector3f velocity;
	private final ImpactProjectile effectImpact;

	public ProjectileControl(CollisionShape shape, float mass, Quaternion rotation, ImpactProjectile effectImpact) {
		super(shape, mass);
		this.effectImpact = effectImpact;
		velocity = rotation.mult(Vector3f.UNIT_Z).mult(INITIAL_SPEED);
	}

	@Override
	public void prePhysicsTick(PhysicsSpace arg0, float tpf) {
		setGravity(gravity);
		setLinearVelocity(velocity);
		fuel -= FUEL_CONSUMPTION_PER_CYCLE * tpf;
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		if (fuel < 0) {
			destroyItem();
		}
	}

	@Override
	public void collision(PhysicsCollisionEvent event) {
		super.collision(event);
		if (event.getObjectA() == this || event.getObjectB() == this) {
			destroyItem();

			if (effectImpact != null && getSpatial().getParent() != null) {
				effectImpact.play(getSpatial().getWorldTranslation());
			}
		}
	}
}
