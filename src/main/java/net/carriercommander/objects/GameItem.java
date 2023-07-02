package net.carriercommander.objects;

import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.scene.Node;
import net.carriercommander.control.BaseControl;

public class GameItem extends Node {

	public GameItem(String name) {
		super(name);
	}

	public PhysicsRigidBody getControl() {
		return getControl(BaseControl.class);
	}

	public void destroy() {
		removeControl(BaseControl.class);
	}
}
