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

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.jme3.water.WaterFilter;

/**
 * This control allows a spatial to float at a water surface created by the WaterFilter.
 * 
 * @author Michael Neuweiler
 */
public class FloatingControl extends AbstractControl {
	private Quaternion currentRotation = new Quaternion();
	Vector3f rotationOffset = new Vector3f();
	private float meterBelowWater, percentageDisplacement, force;
	private RigidBodyControl rbc = null;

	private float width = 5, length = 10, height = 2.5f; // TODO read dimensions from spatial instead
	private float verticalOffset = 0;
	private WaterFilter water = null;

	@Override
	protected void controlUpdate(float tpf) {
		if (rbc == null) {
			rbc = spatial.getControl(RigidBodyControl.class);
		} else {
			meterBelowWater = verticalOffset - rbc.getPhysicsLocation().getY();
			if (water != null) {
				meterBelowWater += water.getWaterHeight();
			}
			if (meterBelowWater > height)
				meterBelowWater = height;
			if (meterBelowWater < -height)
				meterBelowWater = -height;
			percentageDisplacement = meterBelowWater / height;
			force = (rbc.getMass() + percentageDisplacement * rbc.getMass()) * 9.81f;

			rbc.getPhysicsRotation(currentRotation);
			currentRotation.mult(Vector3f.UNIT_Y, rotationOffset);
			rbc.applyForce(new Vector3f(0f, force, 0f), new Vector3f(width * rotationOffset.x, rotationOffset.y, length * rotationOffset.z));

			// System.out.printf("vector: x=%f\ty=%f\tz=%f\tbelow water=%.3fm\tdelta " + "displacement=%.2f%%\tforce=%.0fkN\n", rotationOffset.x,
			// rotationOffset.y, rotationOffset.z, meterBelowWater, percentageDisplacement * 100, force / 1000);
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
	}

	public float getVerticalOffset() {
		return verticalOffset;
	}

	public void setVerticalOffset(float verticalOffset) {
		this.verticalOffset = verticalOffset;
	}

	public WaterFilter getWater() {
		return water;
	}

	public void setWater(WaterFilter water) {
		this.water = water;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}
