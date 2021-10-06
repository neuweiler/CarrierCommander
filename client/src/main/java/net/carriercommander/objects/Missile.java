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
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import net.carriercommander.control.MissileControl;
import net.carriercommander.effects.ExplosionSmall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Missile
 *
 * @author Michael Neuweiler
 */
public class Missile extends PlayerUnit {
	private static final Logger logger = LoggerFactory.getLogger(PlayerUnit.class);
	public static final float WIDTH = .2f, LENGTH = 1.5f, HEIGHT = .2f, MASS = .1f;

	public Missile(String name, AssetManager assetManager, BulletAppState phsyicsState, CameraNode camNode, RenderManager renderManager, Node target) {
		super(name, camNode);

		attachChild(loadModel(assetManager));

		createCameraHooks();
		CollisionShape collisionShape = createCollisionShape();

		ExplosionSmall explosion = new ExplosionSmall(assetManager, renderManager, target.getParent());
		createMissileControl(phsyicsState, collisionShape, target, explosion);
	}

	public static Spatial loadModel(AssetManager assetManager) {
//		Spatial model = assetManager.loadModel("Models/HoverTank/tankFinalExport.blend");
//		model.move(0, -1, 0);
//		model.rotate(0, FastMath.DEG_TO_RAD * 180, 0);
//		logger.debug("vertices: {} triangles: {}", model.getVertexCount(), model.getTriangleCount());
//		return model;

		Box model = new Box(WIDTH, HEIGHT, LENGTH);
		Geometry geometry = new Geometry("Box", model);
		Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", ColorRGBA.Blue);
//		material.setTexture("ColorMap", assetManager.loadTexture("Textures/Terrain/BrickWall/BrickWall.jpg"));
		geometry.setMaterial(material);

		return geometry;
	}

	public static CollisionShape createCollisionShape() {
		return new BoxCollisionShape(new Vector3f(WIDTH, HEIGHT, LENGTH));
	}

	private void createMissileControl(BulletAppState phsyicsState, CollisionShape collisionShape, Node target, ExplosionSmall explosion) {
		control = new MissileControl(collisionShape, target, MASS, explosion);
		control.setRudderPositionZ(LENGTH / 2);
		addControl(control);
		control.setDamping(0.7f, 0.7f);
		phsyicsState.getPhysicsSpace().add(control);
	}

	private void createCameraHooks() {
		camHookFront = new Node();
		attachChild(camHookFront);
		camHookFront.setLocalTranslation(0, .8f, 1.5f);
		camHookFront.rotate(0, FastMath.PI, 0);
	}
}
