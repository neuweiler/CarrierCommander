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
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Spatial;
import com.jme3.water.WaterFilter;
import net.carriercommander.control.ShipControl;
import net.carriercommander.objects.resources.ResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Supply Drone
 *
 * @author Michael Neuweiler
 */
public class SupplyDrone extends GameItem {
	private static final Logger logger = LoggerFactory.getLogger(SupplyDrone.class);
	public static final float WIDTH = 10f, LENGTH = 10f, HEIGHT = 5f, MASS = 50f;
	private ResourceManager resourceManager = new ResourceManager();

	public SupplyDrone(String name, AssetManager assetManager, WaterFilter water, CameraNode camNode) {
		super(name);

		attachChild(loadModel(assetManager));

		CollisionShape collisionShape = createCollisionShape();
		createShipControl(collisionShape, water);
	}

	public static Spatial loadModel(AssetManager assetManager) {
//TODO replace with own model
		Spatial model = assetManager.loadModel("Models/Manta/manta.mesh.xml");
		model.move(0, -1, 0);
		model.setShadowMode(ShadowMode.CastAndReceive);
		logger.debug("vertices: {} triangles: {}", model.getVertexCount(), model.getTriangleCount());
		return model;
	}

	public static CollisionShape createCollisionShape() {
		return new BoxCollisionShape(new Vector3f(WIDTH, HEIGHT, LENGTH));
	}

	private ShipControl createShipControl(CollisionShape collisionShape, WaterFilter water) {
		ShipControl shipControl = new ShipControl(collisionShape, MASS, water);

		shipControl.setRudderPositionZ(LENGTH / 2);
		shipControl.setVerticalOffset(-2);
		shipControl.setDimensions(WIDTH, LENGTH, HEIGHT);

		addControl(shipControl);
		shipControl.setDamping(0.9f, 0.9f);
		return shipControl;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}
}
