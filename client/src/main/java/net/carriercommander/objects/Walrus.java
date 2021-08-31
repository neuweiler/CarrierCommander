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
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.water.WaterFilter;
import net.carriercommander.control.FloatControl;
import net.carriercommander.control.ShipControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Walrus
 *
 * @author Michael Neuweiler
 */
public class Walrus extends PlayerUnit {
	private final Logger logger = LoggerFactory.getLogger(Walrus.class);
	private final float WIDTH = 3.8f, LENGTH = 10f, HEIGHT = 3.2f, MASS = 5f;

	public Walrus(String name, AssetManager assetManager, BulletAppState phsyicsState, WaterFilter water, CameraNode camNode) {
		super(name, assetManager, phsyicsState, water, camNode);

		loadModel(assetManager);
		createCameraHooks();
		CollisionShape collisionShape = createCollisionShape();

		createShipControl(phsyicsState, collisionShape);
		createFloatControl(water);
	}

	private void loadModel(AssetManager assetManager) {
		Spatial model = assetManager.loadModel("Models/BTR80/br01.fbx");
		model.scale(0.05f);
		model.move(0,6.8f,0);
		attachChild(model);
		logger.debug("vertices: {} triangles: {}", getVertexCount(), getTriangleCount());
	}

	private CollisionShape createCollisionShape() {
		CompoundCollisionShape comp = new CompoundCollisionShape();
		comp.addChildShape(new BoxCollisionShape(new Vector3f(WIDTH, HEIGHT, LENGTH)), new Vector3f(0, 3.3f, 0));
		return comp;
	}

	private void createShipControl(BulletAppState phsyicsState, CollisionShape comp) {
		shipControl = new ShipControl(comp, MASS);
		shipControl.setRudderPositionZ(LENGTH / 2);
		addControl(shipControl);
		shipControl.setDamping(0.2f, 0.3f);
		phsyicsState.getPhysicsSpace().add(shipControl);
	}

	private void createFloatControl(WaterFilter water) {
		FloatControl floatControl = new FloatControl();
		floatControl.setWater(water);
		floatControl.setVerticalOffset(-2);
		floatControl.setWidth(WIDTH);
		floatControl.setHeight(HEIGHT);
		floatControl.setLength(LENGTH);
		addControl(floatControl);
	}

	private void createCameraHooks() {
		camHookFront = new Node();
		attachChild(camHookFront);
		camHookFront.setLocalTranslation(0, 8, 3);
		camHookFront.rotate(0, FastMath.DEG_TO_RAD * 180, 0);

		camHookRear = new Node();
		attachChild(camHookRear);
		camHookRear.setLocalTranslation(0, 7, 5);
//		camHookRear.rotate(0, FastMath.DEG_TO_RAD * 90, 0);
	}
}
