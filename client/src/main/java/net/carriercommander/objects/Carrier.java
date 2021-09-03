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
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
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
 * Carrier
 *
 * @author Michael Neuweiler
 */
public class Carrier extends PlayerUnit {
	private static final Logger logger = LoggerFactory.getLogger(Carrier.class);
	public static final float MASS = 100000; // a carrier weighs 100'000 tons
	private Node camHookFlightDeck = null;
	private Node camHookLaser = null;
	private Node camHookFlareLauncher = null;
	private Node camHookSurfaceMissile = null;

	public Carrier(String name, AssetManager assetManager, BulletAppState phsyicsState, WaterFilter water, CameraNode camNode) {
		super(name, assetManager, phsyicsState, water, camNode);

		attachChild(loadModel(assetManager));
		createCameraHooks();
		CollisionShape collisionShape = createCollisionShape();

		createShipControl(phsyicsState, collisionShape);
		createFloatControl(water);

		createAudio(assetManager);
	}

	public static Spatial loadModel(AssetManager assetManager) {
		Spatial model = assetManager.loadModel("Models/AdmiralKuznetsovClasscarrier/carrier.obj");
		logger.debug("vertices: {} triangles: {}", model.getVertexCount(), model.getTriangleCount());
		return model;
	}

	private void createCameraHooks() {
		camHookFront = new Node();
		attachChild(camHookFront);
		camHookFront.setLocalTranslation(0, 40, 0);
		camHookFront.rotate(0, FastMath.DEG_TO_RAD * 180, 0);

		camHookRear = new Node();
		attachChild(camHookRear);
		camHookRear.setLocalTranslation(-40, 10, 170);
		camHookRear.rotate(0, FastMath.DEG_TO_RAD * 130, 0);

		camHookFlightDeck = new Node();
		attachChild(camHookFlightDeck);
		camHookFlightDeck.setLocalTranslation(0, 15, 0);
		camHookFlightDeck.rotate(0, FastMath.DEG_TO_RAD * 180, 0);

		camHookLaser = new Node();
		attachChild(camHookLaser);
		camHookLaser.setLocalTranslation(0, 20, 20);
		camHookLaser.rotate(0, FastMath.DEG_TO_RAD * 180, 0);

		camHookFlareLauncher = new Node();
		attachChild(camHookFlareLauncher);
		camHookFlareLauncher.setLocalTranslation(0, 15, 10);
		camHookFlareLauncher.rotate(0, FastMath.DEG_TO_RAD * 180, 0);

		camHookSurfaceMissile = new Node();
		attachChild(camHookSurfaceMissile);
		camHookSurfaceMissile.setLocalTranslation(0, 15, 10);
		camHookSurfaceMissile.rotate(0, FastMath.DEG_TO_RAD * 0, 0);
	}

	public static CompoundCollisionShape createCollisionShape() {
		CompoundCollisionShape comp = new CompoundCollisionShape(); // use a simple compound shape to improve performance drastically!
		comp.addChildShape(new BoxCollisionShape(new Vector3f(20, 13, 149)), new Vector3f(0f, -1f, -25f));
		comp.addChildShape(new BoxCollisionShape(new Vector3f(7, 19, 30)), new Vector3f(30f, 30f, 5f));
		return comp;
	}

	private void createShipControl(BulletAppState phsyicsState, CollisionShape collisionShape) {
		shipControl = new ShipControl(collisionShape, MASS);
		shipControl.setRudderPositionZ(100);
		addControl(shipControl);
		shipControl.setFriction(0.5f);
		shipControl.setDamping(0.2f, 0.3f);
		phsyicsState.getPhysicsSpace().add(shipControl);
	}

	private void createFloatControl(WaterFilter water) {
		FloatControl floatControl = new FloatControl();
		floatControl.setWater(water);
		floatControl.setVerticalOffset(3.7f);
		floatControl.setWidth(50);
		floatControl.setLength(100);
		floatControl.setHeight(20);
		addControl(floatControl);
	}

	private void createAudio(AssetManager assetManager) {
		audio = new AudioNode(assetManager, "Sound/carrierEngine.ogg", AudioData.DataType.Buffer);
		audio.setLooping(true);
		audio.setPositional(true);
		audio.setVolume(3);
		this.attachChild(audio);
//		audio.play();
	}

	public void setCameraToFlightDeck() {
		setCameraNode(camHookFlightDeck);
	}

	public void setCameraToLaser() {
		setCameraNode(camHookLaser);
	}

	public void setCameraToFlareLauncher() {
		setCameraNode(camHookFlareLauncher);
	}

	public void setCameraToSurfaceMissile() {
		setCameraNode(camHookSurfaceMissile);
	}
}
