package net.carriercommander.control;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseControl extends RigidBodyControl implements PhysicsCollisionListener, PhysicsTickListener {
	private static final Logger logger = LoggerFactory.getLogger(BaseControl.class);
	protected float health = 1.0f;

	public BaseControl(CollisionShape shape, float mass) {
		super(shape, mass);
	}

	@Override
	public void setPhysicsSpace(PhysicsSpace space) {
		super.setPhysicsSpace(space);
		if (space != null) {
			space.addCollisionListener(this);
			space.addTickListener(this);
		}
	}

	@Override
	public void prePhysicsTick(PhysicsSpace space, float timeStep) {
	}

	@Override
	public void physicsTick(PhysicsSpace space, float timeStep) {
	}

	@Override
	public void collision(PhysicsCollisionEvent event) {
		if (event.getObjectA() == this || event.getObjectB() == this) {
			logger.info("collision between {} and {} with impulse {}", event.getObjectA(), event.getObjectB(), event.getAppliedImpulse());
		}
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	protected void removeItem() {
		if (getPhysicsSpace() != null) {
			getPhysicsSpace().removeCollisionListener(this);
			getPhysicsSpace().removeTickListener(this);
			getPhysicsSpace().remove(this);
		}
		if (getSpatial() != null) {
			getSpatial().removeFromParent();
		}
	}

}
