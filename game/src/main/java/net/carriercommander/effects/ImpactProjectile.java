package net.carriercommander.effects;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import net.carriercommander.ui.AbstractState;

public class ImpactProjectile extends Node {
	private static final boolean POINT_SPRITE = true;
	private static final ParticleMesh.Type EMITTER_TYPE = POINT_SPRITE ? ParticleMesh.Type.Point : ParticleMesh.Type.Triangle;
	private static final float FX_TIME = 2f;

	private final AbstractState state;
	private final AssetManager assetManager;
	private ParticleEmitter flash, spark;
	private float curTime = -1.0f;

	public ImpactProjectile(AbstractState state) {
		this.state = state;
		this.assetManager = state.getApplication().getAssetManager();

		createFlash();
		createSpark();

		state.getApplication().getRenderManager().preloadScene(this);
	}

	public void play(Vector3f position) {
		curTime = 0;
		setLocalTranslation(position);
		state.getRootNode().attachChild(this);

		flash.emitAllParticles();
		spark.emitAllParticles();
	}

	@Override
	public void updateLogicalState(float tpf) {
		curTime += tpf;
		if (curTime > FX_TIME) {
			curTime = -1;
			removeFromParent();
		}
	}

	private void createFlash() {
		flash = new ParticleEmitter("Flash", EMITTER_TYPE, 10);
		flash.setSelectRandomImage(true);
		flash.setStartColor(new ColorRGBA(0f, 0.8f, 0.8f, 1f));
		flash.setEndColor(new ColorRGBA(1f, 0.5f, 0.36f, 0.3f));
		flash.setStartSize(.1f);
		flash.setEndSize(15.0f);
		flash.setShape(new EmitterSphereShape(Vector3f.ZERO, .05f));
		flash.setParticlesPerSec(0);
		flash.setGravity(0, 0, 0);
		flash.setLowLife(.2f);
		flash.setHighLife(.4f);
		flash.getParticleInfluencer().setInitialVelocity(new Vector3f(5, 5, 5));
		flash.getParticleInfluencer().setVelocityVariation(3);
		flash.setImagesX(2);
		flash.setImagesY(2);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flash.png"));
		mat.setBoolean("PointSprite", POINT_SPRITE);
		flash.setMaterial(mat);
		attachChild(flash);
	}

	private void createSpark() {
		spark = new ParticleEmitter("Spark", ParticleMesh.Type.Triangle, 30);
		spark.setStartColor(new ColorRGBA(.3f, 1f, 1f, 1.0f));
		spark.setEndColor(new ColorRGBA(1f, 0.5f, 0.36f, 0f));
		spark.setStartSize(.01f);
		spark.setEndSize(2f);
		spark.setFacingVelocity(true);
		spark.setParticlesPerSec(0);
		spark.setGravity(0, 5, 0);
		spark.setLowLife(0f);
		spark.setHighLife(1f);
		spark.getParticleInfluencer().setInitialVelocity(new Vector3f(20, 20, 20));
		spark.getParticleInfluencer().setVelocityVariation(1);
		spark.setImagesX(1);
		spark.setImagesY(1);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/spark.png"));
		spark.setMaterial(mat);
		attachChild(spark);
	}
}
