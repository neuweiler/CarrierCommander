package net.carriercommander.objects;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import net.carriercommander.Constants;
import net.carriercommander.control.ProjectileControl;
import net.carriercommander.effects.ImpactProjectile;
import net.carriercommander.ui.AbstractState;

public class Projectile extends GameItem {
	private static Material material = null;
	public static final float RADIUS = .1f, LENGTH = .9f, MASS = .0005f;
	private static final Cylinder cylinder = new Cylinder(2, 6, RADIUS, LENGTH, true);

	public Projectile(AbstractState state, String name, Quaternion rotation) {
		super(name);

		attachChild(loadModel(state.getApplication().getAssetManager()));

		CollisionShape collisionShape = createCollisionShape();

		ImpactProjectile explosion = new ImpactProjectile(state);
		createProjectileControl(collisionShape, rotation, explosion);
	}

	private static void initMaterial(AssetManager assetManager) {
		if (material != null)
			return;

		material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", ColorRGBA.Yellow);
		material.setColor("GlowColor", ColorRGBA.Yellow.mult(20));
	}

	public static Spatial loadModel(AssetManager assetManager) {
		initMaterial(assetManager);
		Geometry model = new Geometry(Constants.PROJECTILE + System.currentTimeMillis(), cylinder);
		model.setMaterial(material);
		return model;
	}

	public static CollisionShape createCollisionShape() {
		return new BoxCollisionShape(new Vector3f(RADIUS, RADIUS, LENGTH));
	}

	private void createProjectileControl(CollisionShape collisionShape, Quaternion rotation, ImpactProjectile explosion) {
		ProjectileControl control = new ProjectileControl(collisionShape, MASS, rotation, explosion);
		addControl(control);
	}
}
