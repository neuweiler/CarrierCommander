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
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import net.carriercommander.control.PlaneControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manta
 *
 * @author Michael Neuweiler
 */
public class Manta extends PlayerItem {
	private static final Logger logger = LoggerFactory.getLogger(PlayerItem.class);
	public static final float WIDTH = 4.8f, LENGTH = 5.4f, HEIGHT = 2f, MASS = 5f;

	public Manta(String name, AssetManager assetManager, BulletAppState phsyicsState, CameraNode camNode) {
		super(name, camNode);

		attachChild(loadModel(assetManager));

		createCameraHooks();
		CollisionShape collisionShape = createCollisionShape();

		createPlaneControl(phsyicsState, collisionShape);
	}

	public static Spatial loadModel(AssetManager assetManager) {
		Spatial model = assetManager.loadModel("Models/Manta/manta.mesh.xml");
		model.move(0, -1, 0);
		model.setShadowMode(ShadowMode.CastAndReceive);
		logger.debug("vertices: {} triangles: {}", model.getVertexCount(), model.getTriangleCount());
		return model;
	}

	public static CollisionShape createCollisionShape() {
		return new BoxCollisionShape(new Vector3f(WIDTH, HEIGHT, LENGTH));
	}

	private void createPlaneControl(BulletAppState phsyicsState, CollisionShape collisionShape) {
		PlaneControl control = new PlaneControl(collisionShape, MASS);
		addControl(control);
		control.setDamping(0.1f, 0.5f);
		phsyicsState.getPhysicsSpace().add(control);
	}

	private void createCameraHooks() {
		camHookFront = new Node();
		attachChild(camHookFront);
		camHookFront.setLocalTranslation(0, 2.5f, -2);

		camHookRear = new Node();
		attachChild(camHookRear);
		camHookRear.setLocalTranslation(0, 2.5f, -2);
		camHookRear.rotate(0, FastMath.PI, 0);
	}
}
