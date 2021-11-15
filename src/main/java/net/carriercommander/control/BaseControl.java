package net.carriercommander.control;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import net.carriercommander.Player;
import net.carriercommander.objects.GameItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseControl extends RigidBodyControl implements PhysicsCollisionListener, PhysicsTickListener {
	private static final Logger logger = LoggerFactory.getLogger(BaseControl.class);
	protected float health = 1.0f;
	private Player player = null;

	public BaseControl(CollisionShape shape, float mass) {
		super(shape, mass);
	}

	@Override
	public void setPhysicsSpace(PhysicsSpace newSpace) {
		if (newSpace == null) {
			PhysicsSpace oldSpace = getPhysicsSpace();
			if (oldSpace != null) {
				oldSpace.removeCollisionListener(this);
				oldSpace.removeTickListener(this);
			}
			super.setPhysicsSpace(newSpace);
		} else {
			super.setPhysicsSpace(newSpace);
			newSpace.addCollisionListener(this);
			newSpace.addTickListener(this);
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
			health -= event.getAppliedImpulse() / getMass();
			if (health < 0) {
				removeItem();
			}
		}
	}

	public float getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	protected void removeItem() {
		if (player != null) {
			player.removeItem((GameItem) getSpatial());
		} else {
			logger.error("unable to remove item {} as it doesn't belong to a player", getSpatial());
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
