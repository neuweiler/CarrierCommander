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
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.water.WaterFilter;
import net.carriercommander.StateWater;
import net.carriercommander.control.AmphibiousControl;
import net.carriercommander.control.ShipControl;
import net.carriercommander.ui.AbstractState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Walrus
 *
 * @author Michael Neuweiler
 */
public class Walrus extends PlayerItem {
	private static final Logger logger = LoggerFactory.getLogger(Walrus.class);
	public static final float WIDTH = 3.8f, LENGTH = 8.5f, HEIGHT = 2.2f, MASS = 5f;

	public Walrus(AbstractState state, String name, CameraNode camNode) {
		super(name, camNode);

		Spatial model = loadModel(state.getApplication().getAssetManager());
		attachChild(model);
		createCameraHooks(new Vector3f(0, 7, -.7f), new Vector3f(0, 7, -.7f));

		WaterFilter water = state.getState(StateWater.class).getWater();
		CollisionShape collisionShape = createCollisionShape();
		ShipControl shipControl = createShipControl(collisionShape, water);
		VehicleControl vehicleControl = createVehicleControl(collisionShape);
		createAmphibiousControl(shipControl, vehicleControl, water);
	}

	public static Spatial loadModel(AssetManager assetManager) {
		Spatial model = assetManager.loadModel("Models/Walrus/walrus.fbx");
		model.scale(0.05f);
		model.move(0,-.6f,0);
		model.updateModelBound();
		model.setShadowMode(ShadowMode.CastAndReceive);
		logger.debug("vertices: {} triangles: {}", model.getVertexCount(), model.getTriangleCount());
		return model;
	}

	public static CollisionShape createCollisionShape() {
		CompoundCollisionShape comp = new CompoundCollisionShape();
		comp.addChildShape(new BoxCollisionShape(new Vector3f(WIDTH, HEIGHT, LENGTH)), new Vector3f(0, 3.3f, 0));
		return comp;
	}

	private ShipControl createShipControl(CollisionShape collisionShape, WaterFilter water) {
		ShipControl shipControl = super.createShipControl(collisionShape, water, LENGTH, WIDTH, HEIGHT, MASS, -2);
		shipControl.setDamping(0.2f, 0.7f);
		shipControl.setEnabled(false); // we must enable vehicle first so the wheels get properly attached
		return shipControl;
	}

	private VehicleControl createVehicleControl(CollisionShape comp) {
		float stiffness = 20.0f;
		float compValue = 0.8f;
		float dampValue = 0.9f;

		VehicleControl control = new VehicleControl(comp, MASS);
		addControl(control);

		control.setSuspensionCompression(compValue * 2.0f * FastMath.sqrt(stiffness));
		control.setSuspensionDamping(dampValue * 2.0f * FastMath.sqrt(stiffness));
		control.setSuspensionStiffness(stiffness);
		control.setMaxSuspensionForce(MASS * 5);
		control.setMaxSuspensionTravelCm(100f);

		Vector3f direction = new Vector3f(0, -1, 0);
		Vector3f axle = new Vector3f(-1, 0, 0);

		float scaleFactor = 5f;
		float restLength = .5f;
		float radius = 0.3f * scaleFactor;
		float xOff = .55f * scaleFactor;
		float yOff = .2f * scaleFactor; //.31
		float zOff1 = 1.15f * scaleFactor;
		float zOff2 = 0.39f * scaleFactor;
		float zOff3 = -.55f * scaleFactor;
		float zOff4 = -1.31f * scaleFactor;

		Geometry wheelFrontRight1 = findGeom(this, "wheel_frontright_1");
		control.addWheel(wheelFrontRight1, new Vector3f(-xOff, yOff, zOff1), direction, axle, restLength, radius, true);

		Geometry wheelFrontRight2 = findGeom(this, "wheel_frontright_2");
		control.addWheel(wheelFrontRight2, new Vector3f(-xOff, yOff, zOff2), direction, axle, restLength, radius, true);

		Geometry wheelFrontLeft1 = findGeom(this, "wheel_frontleft_1");
		control.addWheel(wheelFrontLeft1, new Vector3f(xOff, yOff, zOff1), direction, axle, restLength, radius, true);

		Geometry wheelFrontLeft2 = findGeom(this, "wheel_frontleft_2");
		control.addWheel(wheelFrontLeft2, new Vector3f(xOff, yOff, zOff2), direction, axle, restLength, radius, true);


		Geometry wheelRearRight1 = findGeom(this, "wheel_rearright_1");
		control.addWheel(wheelRearRight1, new Vector3f(-xOff, yOff, zOff3), direction, axle, restLength, radius, false);

		Geometry wheelRearRight2 = findGeom(this, "wheel_rearright_2");
		control.addWheel(wheelRearRight2, new Vector3f(-xOff, yOff, zOff4), direction, axle, restLength, radius, false);

		Geometry wheelRearLeft1 = findGeom(this, "wheel_rearleft_1");
		control.addWheel(wheelRearLeft1, new Vector3f(xOff, yOff, zOff3), direction, axle, restLength, radius, false);

		Geometry wheelRearLeft2 = findGeom(this, "wheel_rearleft_2");
		control.addWheel(wheelRearLeft2, new Vector3f(xOff, yOff, zOff4), direction, axle, restLength, radius, false);

		return control;
	}

	private AmphibiousControl createAmphibiousControl(ShipControl shipControl, VehicleControl vehicleControl, WaterFilter water) {
		AmphibiousControl amphibiousControl = new AmphibiousControl(shipControl, vehicleControl, water);

		addControl(amphibiousControl);

		return amphibiousControl;
	}

	private Geometry findGeom(Spatial spatial, String name) {
		if (spatial instanceof Node node) {
			for (int i = 0; i < node.getQuantity(); i++) {
				Spatial child = node.getChild(i);
				Geometry result = findGeom(child, name);
				if (result != null) {
					return result;
				}
			}
		} else if (spatial instanceof Geometry) {
			if (spatial.getName().startsWith(name)) {
				return (Geometry) spatial;
			}
		}
		return null;
	}

	@Override
	public PhysicsRigidBody getControl() {
		return (getControl(ShipControl.class).isEnabled() ? getControl(ShipControl.class) : getControl(VehicleControl.class));
	}
}
