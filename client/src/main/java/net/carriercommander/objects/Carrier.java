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
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
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
	public static final float WIDTH = 30f, LENGTH = 160f, HEIGHT = 14f, MASS = 100000; // a carrier weighs 100'000 tons
	public static final float WIDTH_TOWER = 10, LENGTH_TOWER = 30, HEIGHT_TOWER = 19f;
	private Node camHookFlightDeck = null;
	private Node camHookLaser = null;
	private Node camHookFlareLauncher = null;
	private Node camHookSurfaceMissile = null;

	public Carrier(String name, AssetManager assetManager, BulletAppState phsyicsState, WaterFilter water, CameraNode camNode) {
		super(name, camNode);

		attachChild(loadModel(assetManager));
		createCameraHooks();

		CollisionShape collisionShape = createCollisionShape();
		createShipControl(phsyicsState, collisionShape);
		createFloatControl(water);

		createAudio(assetManager);
	}

	public static Spatial loadModel(AssetManager assetManager) {
		Spatial model = assetManager.loadModel("Models/CarrierPlayer/carrier.obj");
		model.setShadowMode(ShadowMode.CastAndReceive);
		model.move(0, 0, -WIDTH);
		model.rotate(0, FastMath.PI, 0);
		logger.debug("vertices: {} triangles: {}", model.getVertexCount(), model.getTriangleCount());
		return model;
	}

	private void createCameraHooks() {
		camHookFront = new Node();
		attachChild(camHookFront);
//		camHookFront.setLocalTranslation(0, 40, 0);
		camHookFront.setLocalTranslation(0, 40, -280);

		camHookRear = new Node();
		attachChild(camHookRear);
		camHookRear.setLocalTranslation(-40, 10, 170);
		camHookRear.rotate(0, FastMath.DEG_TO_RAD * 310, 0);

		camHookFlightDeck = new Node();
		attachChild(camHookFlightDeck);
		camHookFlightDeck.setLocalTranslation(0, 15, 0);

		camHookLaser = new Node();
		attachChild(camHookLaser);
		camHookLaser.setLocalTranslation(0, 20, 20);

		camHookFlareLauncher = new Node();
		attachChild(camHookFlareLauncher);
		camHookFlareLauncher.setLocalTranslation(0, 15, 10);

		camHookSurfaceMissile = new Node();
		attachChild(camHookSurfaceMissile);
		camHookSurfaceMissile.setLocalTranslation(0, 15, 10);
		camHookSurfaceMissile.rotate(0, FastMath.PI, 0);
	}

	public static CompoundCollisionShape createCollisionShape() {
		CompoundCollisionShape comp = new CompoundCollisionShape(); // use a simple compound shape to improve performance drastically!
		comp.addChildShape(new BoxCollisionShape(new Vector3f(WIDTH, HEIGHT, LENGTH)), new Vector3f(0, 0, 0));
		comp.addChildShape(new BoxCollisionShape(new Vector3f(WIDTH_TOWER, HEIGHT_TOWER, LENGTH_TOWER)), new Vector3f(WIDTH_TOWER * -3, HEIGHT + HEIGHT_TOWER, -LENGTH_TOWER - 10));
		return comp;
	}

	private void createShipControl(BulletAppState phsyicsState, CollisionShape collisionShape) {
		control = new ShipControl(collisionShape, MASS);
		control.setRudderPositionZ(LENGTH);
		addControl(control);
		control.setFriction(0.5f);
		control.setDamping(0.2f, 0.3f);
		phsyicsState.getPhysicsSpace().add(control);
	}

	private void createFloatControl(WaterFilter water) {
		FloatControl floatControl = new FloatControl();
		floatControl.setWater(water);
		floatControl.setVerticalOffset(3.7f);
		floatControl.setWidth(WIDTH);
		floatControl.setLength(LENGTH);
		floatControl.setHeight(HEIGHT);
		addControl(floatControl);
	}

	private void createAudio(AssetManager assetManager) {
/*		audio = new AudioNode(assetManager, "Sound/carrierEngine.ogg", AudioData.DataType.Buffer);
		audio.setLooping(true);
		audio.setPositional(true);
		audio.setVolume(3);
		this.attachChild(audio);
		audio.play();*/
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
