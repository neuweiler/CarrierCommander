package net.carriercommander.effects;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import net.carriercommander.ui.AbstractState;

public class TrailMissile extends Node {
	private static final long KEEP_ALIVE_AFTER_STOP = 5000;
	private final AbstractState state;
	private final AssetManager assetManager;
	private final Node missile;
	private ParticleEmitter smoketrail;
	private boolean stopped = false;
	private long stopTime = 0;
	private static Material mat = null;

	public TrailMissile(AbstractState state, Node missile) {
		this.state = state;
		this.assetManager = state.getApplication().getAssetManager();
		this.missile = missile;

		createSmokeTrail();
		state.getApplication().getRenderManager().preloadScene(this);
	}

	public void play() {
		state.getRootNode().attachChild(this);
		smoketrail.emitAllParticles();
	}

	public void stop() {
		smoketrail.setParticlesPerSec(0);
		stopTime = System.currentTimeMillis() + KEEP_ALIVE_AFTER_STOP;
		stopped = true;
	}

	@Override
	public void updateLogicalState(float tpf) {
		setLocalTranslation(missile.getLocalTranslation());
		if (stopped && stopTime < System.currentTimeMillis()) {
			this.removeFromParent();
		}
	}

	private void createSmokeTrail() {
		if (mat == null) {
			mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
//		mat.getAdditionalRenderState().setDepthWrite(true); // fixes invisible particles in front of water
			mat.setTexture("Texture", assetManager.loadTexture("Effects/Smoke/Smoke.png"));
		}

		smoketrail = new ParticleEmitter("Smoketrail" + missile.getName(), ParticleMesh.Type.Triangle, 300);
		smoketrail.setGravity(0, 0, 0);
		smoketrail.setLowLife(0);
		smoketrail.setHighLife(5);
		smoketrail.setEndSize(5);
//		smoketrail.setParticlesPerSec(100);
		smoketrail.setImagesX(15);
		smoketrail.setMaterial(mat);
		attachChild(smoketrail);
	}
}
