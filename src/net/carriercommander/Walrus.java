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
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.water.WaterFilter;

public class Walrus extends Node {

	Walrus(AssetManager assetManager, BulletAppState phsyicsState, float initialWaterHeight, WaterFilter water) {
		Spatial model = assetManager.loadModel("Models/BTR80/BTR.obj");
		model.scale(0.1f);
		model.rotate((float) Math.toRadians(-90), 0, 0);
		attachChild(model);

		System.out.println("walrus vertices: " + getVertexCount() + " triangles: " + getTriangleCount());
		setLocalTranslation(-500, initialWaterHeight + 2, 300);

		BoxCollisionShape collisionShape = new BoxCollisionShape(new Vector3f(20, 5.5f, 8.5f));
		RigidBodyControl control = new RigidBodyControl(collisionShape, 5000);
		addControl(control);
		control.setDamping(0.2f, 0.1f);
		phsyicsState.getPhysicsSpace().add(control);

		FloatingControl floatingControl = new FloatingControl();
		floatingControl.setWater(water);
		floatingControl.setVerticalOffset(2);
		floatingControl.setWidth(3);
		floatingControl.setLength(7);
		floatingControl.setHeight(2.5f);
		addControl(floatingControl);

		control.setLinearVelocity(getLocalRotation().getRotationColumn(0).mult(-30));
	}
}
