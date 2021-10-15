package net.carriercommander.control;

import com.jme3.bullet.control.VehicleControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.jme3.water.WaterFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmphibiousControl extends AbstractControl {
	private static final Logger logger = LoggerFactory.getLogger(AmphibiousControl.class);

	private final ShipControl shipControl;
	private final VehicleControl vehicleControl;
	private final WaterFilter water;
	private float throttle = 0;

	public AmphibiousControl(ShipControl shipControl, VehicleControl vehicleControl, WaterFilter water) {
		this.shipControl = shipControl;
		this.vehicleControl = vehicleControl;
		this.water = water;
	}

	Vector3f velocity = new Vector3f();
	@Override
	protected void controlUpdate(float tpf) {
		float waterHeight = (water != null ? water.getWaterHeight() : 0);
		if ((spatial.getWorldTranslation().getY()) < waterHeight) { // switch from vehicle to ship
			if (vehicleControl.isEnabled()) {
				velocity = vehicleControl.getLinearVelocity();
				vehicleControl.setEnabled(false);
				return;
			}
			if (!shipControl.isEnabled()) {
				shipControl.setLinearVelocity(velocity);
				shipControl.setEnabled(true);
				shipControl.clearForces();
				shipControl.setLinearVelocity(velocity);
				logger.info("{} has reached the shore", shipControl.getSpatial().getName());
			}
		} else { // switch from ship to vehicle
			if (shipControl.isEnabled()) {
				shipControl.setEnabled(false);
				throttle = shipControl.getThrottle();
				velocity = shipControl.getLinearVelocity();
				return;
			}
			if (!vehicleControl.isEnabled()) {
				vehicleControl.setEnabled(true);
				shipControl.clearForces();
				vehicleControl.setLinearVelocity(velocity);
				vehicleControl.accelerate(throttle * 10000);
				logger.info("{} has reached the beach of {}", shipControl.getSpatial().getName(), "tbd");
			}
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
	}

}
