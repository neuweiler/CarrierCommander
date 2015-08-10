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

package net.carriercommander;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Carrier extends Node {

	private Node camHookCarrierBridge = null;
	private Node camHookCarrierRear = null;
	private Node camHookCarrierFlightDeck = null;
	private Node camHookCarrierLaser = null;
	private Node camHookCarrierFlareLauncher = null;
	private Node camHookCarrierSurfaceMissile = null;
	public final float weigth = 100000000; // a carrier weighs 100'000 tons

	public Carrier(AssetManager assetManager, BulletAppState phsyicsState, float initialWaterHeight) {
		super();
		Spatial model = assetManager.loadModel("Models/AdmiralKuznetsovClasscarrier/carrier.obj");
		attachChild(model);
		System.out.println("carrier vertices: " + model.getVertexCount() + " triangles: " + model.getTriangleCount());

		camHookCarrierBridge = new Node();
		attachChild(camHookCarrierBridge);
		camHookCarrierBridge.setLocalTranslation(0, 20, 0);
		camHookCarrierBridge.rotate(0, FastMath.DEG_TO_RAD * 180, 0);

		camHookCarrierRear = new Node();
		attachChild(camHookCarrierRear);
		camHookCarrierRear.setLocalTranslation(-40, 10, 170);
		camHookCarrierRear.rotate(0, FastMath.DEG_TO_RAD * 130, 0);

		camHookCarrierFlightDeck = new Node();
		attachChild(camHookCarrierFlightDeck);
		camHookCarrierFlightDeck.setLocalTranslation(0, 15, 0);
		camHookCarrierFlightDeck.rotate(0, FastMath.DEG_TO_RAD * 180, 0);

		camHookCarrierLaser = new Node();
		attachChild(camHookCarrierLaser);
		camHookCarrierLaser.setLocalTranslation(0, 20, 20);
		camHookCarrierLaser.rotate(0, FastMath.DEG_TO_RAD * 180, 0);

		camHookCarrierFlareLauncher = new Node();
		attachChild(camHookCarrierFlareLauncher);
		camHookCarrierFlareLauncher.setLocalTranslation(0, 15, 10);
		camHookCarrierFlareLauncher.rotate(0, FastMath.DEG_TO_RAD * 180, 0);

		camHookCarrierSurfaceMissile = new Node();
		attachChild(camHookCarrierSurfaceMissile);
		camHookCarrierSurfaceMissile.setLocalTranslation(0, 15, 10);
		camHookCarrierSurfaceMissile.rotate(0, FastMath.DEG_TO_RAD * 0, 0);

		setLocalTranslation(-600, initialWaterHeight + 50, 400);

		CompoundCollisionShape comp = new CompoundCollisionShape(); // use a simple compound shape to improve performance drastically!
		comp.addChildShape(new BoxCollisionShape(new Vector3f(20, 13, 149)), new Vector3f(0f, -1f, -25f));
		comp.addChildShape(new BoxCollisionShape(new Vector3f(7, 19, 30)), new Vector3f(30f, 30f, 5f));
		RigidBodyControl control = new RigidBodyControl(comp, weigth);
		control.setFriction(0.5f);
		control.setDamping(0.5f, 0.3f);
		addControl(control);
		phsyicsState.getPhysicsSpace().add(control);

//		control.setGravity(new Vector3f());
//		control.setAngularVelocity(new Vector3f(0f, 0f, 0.1f));
//		control.setLinearVelocity(getLocalRotation().getRotationColumn(2).mult(-10));

		float angles[] = { FastMath.DEG_TO_RAD * 10, FastMath.DEG_TO_RAD * 0, FastMath.DEG_TO_RAD * 45 }; // pitch, yaw, roll
		control.setPhysicsRotation(new Quaternion(angles));

	}

	public void setCameraToBridge(CameraNode camNode) {
		if (camNode.getParent() != null)
			camNode.getParent().detachChild(camNode);
		camHookCarrierBridge.attachChild(camNode);
	}

	public void setCameraToRear(CameraNode camNode) {
		if (camNode.getParent() != null)
			camNode.getParent().detachChild(camNode);
		camHookCarrierRear.attachChild(camNode);
	}

	public void setCameraToFlightDeck(CameraNode camNode) {
		if (camNode.getParent() != null)
			camNode.getParent().detachChild(camNode);
		camHookCarrierFlightDeck.attachChild(camNode);
	}

	public void setCameraToLaser(CameraNode camNode) {
		if (camNode.getParent() != null)
			camNode.getParent().detachChild(camNode);
		camHookCarrierLaser.attachChild(camNode);
	}

	public void setCameraToFlareLauncher(CameraNode camNode) {
		if (camNode.getParent() != null)
			camNode.getParent().detachChild(camNode);
		camHookCarrierFlareLauncher.attachChild(camNode);
	}

	public void setCameraToSurfaceMissile(CameraNode camNode) {
		if (camNode.getParent() != null)
			camNode.getParent().detachChild(camNode);
		camHookCarrierSurfaceMissile.attachChild(camNode);
	}
}
