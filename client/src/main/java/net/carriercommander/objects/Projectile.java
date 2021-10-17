package net.carriercommander.objects;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import net.carriercommander.control.ProjectileControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Projectile extends GameItem {
	private static final Logger logger = LoggerFactory.getLogger(Projectile.class);
	private static Material material = null;
	public static final float RADIUS = .1f, LENGTH = .9f, MASS = .0005f;
	private static final Cylinder cylinder = new Cylinder(2, 6, RADIUS, LENGTH, true);

	public Projectile(String name, AssetManager assetManager, PhysicsSpace physicsSpace, Quaternion rotation) {
		super(name);

		initMaterial(assetManager);
		attachChild(loadModel(name, assetManager));

		CollisionShape collisionShape = createCollisionShape();

		createProjectileControl(physicsSpace, collisionShape, rotation);
	}

	private void initMaterial(AssetManager assetManager) {
		if (material != null)
			return;

		material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", ColorRGBA.Yellow);
		material.setColor("GlowColor", ColorRGBA.Yellow.mult(20));
	}

	public static Spatial loadModel(String name, AssetManager assetManager) {
		Geometry model = new Geometry(name, cylinder);
		model.setMaterial(material);
		return model;
	}

	public static CollisionShape createCollisionShape() {
		return new BoxCollisionShape(new Vector3f(RADIUS, RADIUS, LENGTH));
	}

	private void createProjectileControl(PhysicsSpace physicsSpace, CollisionShape collisionShape, Quaternion rotation) {
		ProjectileControl control = new ProjectileControl(collisionShape, MASS, rotation);
		addControl(control);
		physicsSpace.add(control);
	}
}