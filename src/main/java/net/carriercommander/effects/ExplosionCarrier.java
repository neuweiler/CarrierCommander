package net.carriercommander.effects;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import net.carriercommander.ui.AbstractState;

public class ExplosionCarrier extends Node {
	private static final boolean POINT_SPRITE = true;
	private static final ParticleMesh.Type EMITTER_TYPE = POINT_SPRITE ? ParticleMesh.Type.Point : ParticleMesh.Type.Triangle;
	private static final float FX_TIME = 6f;

	private final AbstractState state;
	private final AssetManager assetManager;
	private ParticleEmitter flame, flash, spark, smoketrail, debris, shockwave;
	private float curTime = -1.0f;

	public ExplosionCarrier(AbstractState state) {
		this.state = state;
		this.assetManager = state.getApplication().getAssetManager();

		createFlame();
		createFlash();
		createSpark();
		createSmokeTrail();
		createDebris();
		createShockwave();

		state.getApplication().getRenderManager().preloadScene(this);
	}

	public void play(Vector3f position) {
		curTime = 0;
		setLocalTranslation(position);
		state.getRootNode().attachChild(this);

		flame.emitAllParticles();
		flash.emitAllParticles();
		spark.emitAllParticles();
		smoketrail.emitAllParticles();
		debris.emitAllParticles();
		shockwave.emitAllParticles();
	}

	@Override
	public void updateLogicalState(float tpf) {
		curTime += tpf;
		if (curTime > FX_TIME) {
			curTime = -1;
			removeFromParent();
		}
	}

	private void createFlame() {
		flame = new ParticleEmitter("Flame", EMITTER_TYPE, 32);
		flame.setSelectRandomImage(true);
		flame.setStartColor(new ColorRGBA(1f, 0.4f, 0.05f, 1f));
		flame.setEndColor(new ColorRGBA(.4f, .22f, .12f, 0f));
		flame.setStartSize(4f);
		flame.setEndSize(20f);
		flame.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
		flame.setParticlesPerSec(0);
		flame.setGravity(0, -5, 0);
		flame.setLowLife(.8f);
		flame.setHighLife(1.5f);
		flame.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 17, 0));
		flame.getParticleInfluencer().setVelocityVariation(1f);
		flame.setImagesX(2);
		flame.setImagesY(2);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
		mat.setBoolean("PointSprite", POINT_SPRITE);
		flame.setMaterial(mat);
		attachChild(flame);
	}

	private void createFlash() {
		flash = new ParticleEmitter("Flash", EMITTER_TYPE, 24);
		flash.setSelectRandomImage(true);
		flash.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, 1f));
		flash.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
		flash.setStartSize(.1f);
		flash.setEndSize(15.0f);
		flash.setShape(new EmitterSphereShape(Vector3f.ZERO, .05f));
		flash.setParticlesPerSec(0);
		flash.setGravity(0, 0, 0);
		flash.setLowLife(.2f);
		flash.setHighLife(.4f);
		flash.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 5f, 0));
		flash.getParticleInfluencer().setVelocityVariation(1);
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
		spark.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, 1.0f));
		spark.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
		spark.setStartSize(.5f);
		spark.setEndSize(5f);
		spark.setFacingVelocity(true);
		spark.setParticlesPerSec(0);
		spark.setGravity(0, 5, 0);
		spark.setLowLife(2f);
		spark.setHighLife(3f);
		spark.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 20, 0));
		spark.getParticleInfluencer().setVelocityVariation(1);
		spark.setImagesX(1);
		spark.setImagesY(1);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/spark.png"));
		spark.setMaterial(mat);
		attachChild(spark);
	}

	private void createSmokeTrail() {
		smoketrail = new ParticleEmitter("SmokeTrail", ParticleMesh.Type.Triangle, 50);
		smoketrail.setStartColor(new ColorRGBA(1f, 0.8f, 0.36f, 1.0f));
		smoketrail.setEndColor(new ColorRGBA(1f, 0.8f, 0.36f, 0f));
		smoketrail.setStartSize(.2f);
		smoketrail.setEndSize(4f);
//        smoketrail.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
		smoketrail.setFacingVelocity(true);
		smoketrail.setParticlesPerSec(0);
		smoketrail.setGravity(0, 1, 0);
		smoketrail.setLowLife(5f);
		smoketrail.setHighLife(6f);
		smoketrail.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 10, 0));
		smoketrail.getParticleInfluencer().setVelocityVariation(1);
		smoketrail.setImagesX(1);
		smoketrail.setImagesY(3);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/smoketrail.png"));
		smoketrail.setMaterial(mat);
		attachChild(smoketrail);
	}

	private void createDebris() {
		debris = new ParticleEmitter("Debris", ParticleMesh.Type.Triangle, 15);
		debris.setSelectRandomImage(true);
		debris.setRandomAngle(true);
		debris.setRotateSpeed(FastMath.TWO_PI * 4);
		debris.setStartColor(new ColorRGBA(1f, 1f, 1f, 1f));
		debris.setEndColor(new ColorRGBA(1f, 1f, 1f, 1f));
		debris.setStartSize(.2f);
		debris.setEndSize(1.5f);
//        debris.setShape(new EmitterSphereShape(Vector3f.ZERO, .05f));
		debris.setParticlesPerSec(0);
		debris.setGravity(0, 12f, 0);
		debris.setLowLife(4f);
		debris.setHighLife(6f);
		debris.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 40, 0));
		debris.getParticleInfluencer().setVelocityVariation(.60f);
		debris.setImagesX(3);
		debris.setImagesY(3);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/debris.png"));
		debris.setMaterial(mat);
		attachChild(debris);
	}

	private void createShockwave() {
		shockwave = new ParticleEmitter("Shockwave", ParticleMesh.Type.Triangle, 1);
//		shockwave.setRandomAngle(true);
//		shockwave.setFaceNormal(Vector3f.UNIT_Y);
		shockwave.setStartColor(new ColorRGBA(.48f, 0.17f, 0.01f, .8f));
		shockwave.setEndColor(new ColorRGBA(.48f, 0.17f, 0.01f, 0f));

		shockwave.setStartSize(.01f);
		shockwave.setEndSize(100f);

		shockwave.setParticlesPerSec(0);
		shockwave.setGravity(0, 0, 0);
		shockwave.setLowLife(.5f);
		shockwave.setHighLife(.8f);
		shockwave.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0, 0));
		shockwave.getParticleInfluencer().setVelocityVariation(0f);
		shockwave.setImagesX(1);
		shockwave.setImagesY(1);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/shockwave.png"));
		shockwave.setMaterial(mat);
		attachChild(shockwave);
	}
}
