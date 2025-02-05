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

package net.carriercommander.objects;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import net.carriercommander.control.MissileControl;
import net.carriercommander.effects.ImpactMissile;
import net.carriercommander.effects.TrailMissile;
import net.carriercommander.ui.AbstractState;

/**
 * Missile
 *
 * @author Michael Neuweiler
 */
public class Missile extends GameItem {
	public static final float WIDTH = .2f, LENGTH = 1.1f, HEIGHT = .2f, MASS = .1f;

	private TrailMissile trail;

	public Missile(AbstractState state, String name, Node target) {
		super(name);

		attachChild(loadModel(state.getApplication().getAssetManager()));

		CollisionShape collisionShape = createCollisionShape();

		ImpactMissile explosion = new ImpactMissile(state);
		trail = new TrailMissile(state, this);
		trail.play();
		createMissileControl(collisionShape, target, explosion);
	}

	public static Spatial loadModel(AssetManager assetManager) {
		return assetManager.loadModel("Models/Missile/Rocket.mesh.xml");
	}

	public static CollisionShape createCollisionShape() {
		return new BoxCollisionShape(new Vector3f(WIDTH, HEIGHT, LENGTH));
	}

	private void createMissileControl(CollisionShape collisionShape, Node target, ImpactMissile explosion) {
		MissileControl control = new MissileControl(collisionShape, target, MASS, explosion);
		addControl(control);
		control.setDamping(0.7f, 0.7f);
	}

	@Override
	public void destroy() {
		trail.stop();
		trail = null;
		super.destroy();
	}
}
