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

import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.water.WaterFilter;
import net.carriercommander.control.ShipControl;

/**
 * @author Michael Neuweiler
 */
public abstract class PlayerItem extends GameItem {
	protected CameraNode camNode;
	private boolean rearView = false;
	protected AudioNode audio = null;
	protected Node camHookFront = null;
	protected Node camHookRear = null;

	public enum Type {CARRIER, WALRUS, MANTA}

	PlayerItem(String name, CameraNode camNode) {
		super(name);
		this.camNode = camNode;
	}

	protected ShipControl createShipControl(CollisionShape collisionShape, WaterFilter water, float length, float width, float height, float mass, float veritcalOffset) {
		ShipControl shipControl = new ShipControl(collisionShape, mass, water);

		shipControl.setRudderPositionZ(length / 2);
		shipControl.setVerticalOffset(veritcalOffset);
		shipControl.setDimensions(width, length, height);

		addControl(shipControl);
		return shipControl;
	}

	protected void createCameraHooks(Vector3f positionFront, Vector3f positionRear) {
		camHookFront = new Node();
		attachChild(camHookFront);
		camHookFront.setLocalTranslation(positionFront);

		camHookRear = new Node();
		attachChild(camHookRear);
		camHookRear.setLocalTranslation(positionRear);
		camHookRear.rotate(0, FastMath.PI, 0);
	}

	protected void setCameraNode(Node node) {
		if (camNode.getParent() != null)
			camNode.getParent().detachChild(camNode);
		node.attachChild(camNode);
node.rotate(0,25 * FastMath.DEG_TO_RAD,0);
	}

	public void toggleRearView() {
		if (rearView) {
			setCameraToFront();
		} else {
			setCameraToRear();
		}
	}

	public boolean isRearView() {
		return rearView;
	}

	public void setCameraToFront() {
		setCameraNode(camHookFront);
		rearView = false;
	}

	public void setCameraToRear() {
		setCameraNode(camHookRear);
		rearView = true;
	}

	public abstract Type getType();
}
