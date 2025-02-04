package net.carriercommander.control;

import com.jme3.bullet.control.VehicleControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import com.jme3.water.WaterFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A physics controller for objects which can float on water and drive on the ground (Walrus).
 * If the object is below or above the water height, it switches from VehicleControl to @shipControl and vice versa.
 */

public class AmphibiousControl extends AbstractControl {
	private static final Logger logger = LoggerFactory.getLogger(AmphibiousControl.class);

	private final ShipControl shipControl;
	private final VehicleControl vehicleControl;
	private final WaterFilter water;
	/** use the current throttle to accelerate the new controller **/
	private float throttle = 0;
	/** transfer the velocity of the current controller to the other controller **/
	private Vector3f velocity = new Vector3f();

	public AmphibiousControl(ShipControl shipControl, VehicleControl vehicleControl, WaterFilter water) {
		this.shipControl = shipControl;
		this.vehicleControl = vehicleControl;
		this.water = water;
	}

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
		} else if ((spatial.getWorldTranslation().getY()) > waterHeight + 1f) { // switch from ship to vehicle
			if (shipControl.isEnabled()) {
				throttle = shipControl.getThrottle();
				velocity = shipControl.getLinearVelocity();
				shipControl.setEnabled(false);
				return;
			}
			if (!vehicleControl.isEnabled()) {
				vehicleControl.setEnabled(true);
				shipControl.clearForces();
				vehicleControl.setLinearVelocity(velocity);
				vehicleControl.accelerate(throttle * vehicleControl.getMass());
				logger.info("{} has reached the beach of {}", shipControl.getSpatial().getName(), "tbd");
			}
		}
	}

	@Override
	protected void controlRender(RenderManager rm, ViewPort vp) {
	}

}
