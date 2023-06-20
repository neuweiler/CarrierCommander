package net.carriercommander;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import net.carriercommander.network.model.config.Island;
import net.carriercommander.ui.AbstractState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class StateTerrain extends AbstractState {
	private static final Logger logger = LoggerFactory.getLogger(StateTerrain.class);
	private final List<TerrainQuad> terrains = new ArrayList<>();
	private AssetManager assetManager;
	private BulletAppState physicsState;
	private final List<Island> islands;

	public StateTerrain(List<Island> islands) {
		this.islands = islands;
	}

	@Override
	protected void initialize(Application app) {
		assetManager = app.getAssetManager();
		physicsState = getState(BulletAppState.class);

		Material matRock = createMaterial();

		islands.forEach(island -> {
			logger.debug("creating island {}", island.toString());
			Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/islands/" + island.getModel() + "_height.png");
			AbstractHeightMap heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 0.25f);
			heightmap.load();

			TerrainQuad terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
			terrain.setName(island.getName());

			Material mat = matRock.clone();
			mat.setTexture("AlphaMap", assetManager.loadTexture("Textures/Terrain/islands/" + island.getModel() + "_alpha.png"));

			terrain.setMaterial(mat);
			terrain.setLocalScale(new Vector3f(5, 5, 5));
			terrain.setLocalTranslation(new Vector3f(island.getX(), island.getY(), island.getZ()).multLocal(Constants.MAP_SCENE_FACTOR));
			terrain.setLocked(false); // unlock it so we can edit the height

			RigidBodyControl rbc = new RigidBodyControl(0);
			terrain.addControl(rbc);

			terrain.setShadowMode(RenderQueue.ShadowMode.Receive);

			// TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
			// terrain.addControl(control);

			terrains.add(terrain);
		});

	}

	@Override
	protected void cleanup(Application app) {
		terrains.clear();
	}

	@Override
	protected void onEnable() {
		terrains.forEach(terrain -> {
			physicsState.getPhysicsSpace().add(terrain.getControl(RigidBodyControl.class));
			getRootNode().attachChild(terrain);
		});
	}

	@Override
	protected void onDisable() {
		if (physicsState.getPhysicsSpace() != null) {
			terrains.forEach(terrain -> {
				physicsState.getPhysicsSpace().remove(terrain.getControl(RigidBodyControl.class));
				terrain.removeFromParent();
			});
		}
	}

	private Material createMaterial() {
		Material matRock = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
		matRock.setBoolean("useTriPlanarMapping", false);
		matRock.setBoolean("WardIso", true);

		Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
		grass.setWrap(Texture.WrapMode.Repeat);
		matRock.setTexture("DiffuseMap", grass);
		matRock.setFloat("DiffuseMap_0_scale", 64);

		Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
		dirt.setWrap(Texture.WrapMode.Repeat);
		matRock.setTexture("DiffuseMap_1", dirt);
		matRock.setFloat("DiffuseMap_1_scale", 16);

		Texture rock = assetManager.loadTexture("Textures/Terrain/splat/road.jpg");
		rock.setWrap(Texture.WrapMode.Repeat);
		matRock.setTexture("DiffuseMap_2", rock);
		matRock.setFloat("DiffuseMap_2_scale", 128);

		Texture normalMap0 = assetManager.loadTexture("Textures/Terrain/splat/grass_normal.jpg");
		normalMap0.setWrap(Texture.WrapMode.Repeat);
		Texture normalMap1 = assetManager.loadTexture("Textures/Terrain/splat/dirt_normal.png");
		normalMap1.setWrap(Texture.WrapMode.Repeat);
		Texture normalMap2 = assetManager.loadTexture("Textures/Terrain/splat/road_normal.png");
		normalMap2.setWrap(Texture.WrapMode.Repeat);
		matRock.setTexture("NormalMap", normalMap0);
		matRock.setTexture("NormalMap_1", normalMap2);
		matRock.setTexture("NormalMap_2", normalMap2);

		return matRock;
	}

}
