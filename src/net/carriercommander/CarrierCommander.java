/*
 * Copyright (c) 2015, Michael Neuweiler
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of CarrierCommander nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */

package net.carriercommander;

import java.util.ArrayList;
import java.util.List;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace.BroadphaseType;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.Texture2D;
import com.jme3.util.SkyFactory;
import com.jme3.water.WaterFilter;

import de.lessvoid.nifty.Nifty;

/**
 * Carrier Commander Main Class
 * 
 * @author Michael Neuweiler
 */
public class CarrierCommander extends SimpleApplication {

	private BulletAppState phsyicsState;
	private Vector3f lightDir = new Vector3f(-4.9236743f, -1.27054665f, 5.896916f);
	private WaterFilter water = null;
	TerrainQuad terrain = null;
	Material matRock = null;
	CameraNode camNode = null;

	Carrier carrier = null;
	Walrus walrus = null;
	Manta manta = null;

	private float time = 0.0f;
	private float waterHeight = 0.0f;
	private float initialWaterHeight = 90f;// 0.8f;

	public static void main(String[] args) {
		CarrierCommander app = new CarrierCommander();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		// setDisplayFps(false);
		// setDisplayStatView(false);

		// createNitfyGui();
		activatePhysics();
		configureCamera();

		createTerrain();
		createSky();
		createSun();
		createWater();
		createPostProcessFilter();

		carrier = new Carrier(assetManager, phsyicsState, initialWaterHeight, water);
		rootNode.attachChild(carrier);
		walrus = new Walrus(assetManager, phsyicsState, initialWaterHeight, water);
		rootNode.attachChild(walrus);
		// manta = new Manta(assetManager, phsyicsState, water);
		// rootNode.attachChild(manta);

		// carrier.setCameraToBridge(camNode);
		// camNode.lookAt(target.getLocalTranslation(), Vector3f.UNIT_Y);

		createKeyMappings();
	}

	private void createNitfyGui() {
		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
		Nifty nifty = niftyDisplay.getNifty();
		nifty.fromXml("Interface/screen.xml", "start");
		guiViewPort.addProcessor(niftyDisplay);
	}

	private void activatePhysics() {
		phsyicsState = new BulletAppState();
		phsyicsState.setThreadingType(BulletAppState.ThreadingType.PARALLEL); // do not set while enabling debug !
		phsyicsState.setBroadphaseType(BroadphaseType.SIMPLE);
		stateManager.attach(phsyicsState);

//		phsyicsState.setDebugEnabled(true);
	}

	private void createKeyMappings() {
		inputManager.addListener(new ActionListener() {
			public void onAction(String name, boolean isPressed, float tpf) {
				if (isPressed) {
					if (name.equals("upRM")) {
						water.setReflectionMapSize(Math.min(water.getReflectionMapSize() * 2, 4096));
						System.out.println("Reflection map size : " + water.getReflectionMapSize());
					}
					if (name.equals("downRM")) {
						water.setReflectionMapSize(Math.max(water.getReflectionMapSize() / 2, 32));
						System.out.println("Reflection map size : " + water.getReflectionMapSize());
					}
					if (name.equals("throttle")) {
						// walrusControl.setLinearVelocity(walrus.getLocalRotation().getRotationColumn(0).mult(-50));
					}
				}
			}
		}, "upRM", "downRM", "throttle");
		inputManager.addMapping("throttle", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping("upRM", new KeyTrigger(KeyInput.KEY_PGUP));
		inputManager.addMapping("downRM", new KeyTrigger(KeyInput.KEY_PGDN));
	}

	private void createPostProcessFilter() {
		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);

		if (water != null)
			fpp.addFilter(water);

		BloomFilter bloom = new BloomFilter();
		bloom.setExposurePower(55);
		bloom.setBloomIntensity(1.0f);
		// fpp.addFilter(bloom);

		LightScatteringFilter lsf = new LightScatteringFilter(lightDir.mult(-300));
		lsf.setLightDensity(1.0f);
		// fpp.addFilter(lsf);

		DepthOfFieldFilter dof = new DepthOfFieldFilter();
		dof.setFocusDistance(0);
		dof.setFocusRange(1000);
		// fpp.addFilter(dof);

		// fpp.addFilter(new TranslucentBucketFilter());
		// fpp.setNumSamples(4);

		viewPort.addProcessor(fpp);
	}

	private void createWater() {
		water = new WaterFilter(rootNode, lightDir);

		water.setWaveScale(0.003f);
		water.setMaxAmplitude(2f);
		water.setFoamExistence(new Vector3f(1f, 4, 0.5f));
		water.setFoamTexture((Texture2D) assetManager.loadTexture("Common/MatDefs/Water/Textures/foam2.jpg"));
		// water.setNormalScale(0.5f);
		// water.setRefractionConstant(0.25f);
		// water.setRefractionStrength(0.2f);
		// water.setFoamHardness(0.6f);
		water.setWaterHeight(initialWaterHeight);
	}

	private void createSky() {
		Spatial sky = SkyFactory.createSky(assetManager, "Scenes/Beach/FullskiesSunset0068.dds", false);
		sky.setLocalScale(350);
		rootNode.attachChild(sky);
	}

	private void configureCamera() {
		flyCam.setMoveSpeed(200);
		// flyCam.setEnabled(false); // Disable the default flyby cam

		camNode = new CameraNode("Camera Node", cam);
		camNode.setControlDir(ControlDirection.SpatialToCamera);

		cam.setLocation(new Vector3f(-550, 130, 200));
		cam.setRotation(new Quaternion().fromAngles(new float[] { FastMath.DEG_TO_RAD * 15.0f, FastMath.DEG_TO_RAD * -20f, 0 }));

		cam.setFrustumFar(4000);
		// cam.setFrustumNear(100);
	}

	private void createSun() {
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(lightDir);
		sun.setColor(ColorRGBA.White.clone().multLocal(1.7f));
		rootNode.addLight(sun);
	}

	private void createTerrain() {
		matRock = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
		matRock.setBoolean("useTriPlanarMapping", false);
		matRock.setBoolean("WardIso", true);
		matRock.setTexture("AlphaMap", assetManager.loadTexture("Textures/Terrain/splat/alphamap.png"));
		Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/splat/mountains512.png");
		Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
		grass.setWrap(WrapMode.Repeat);
		matRock.setTexture("DiffuseMap", grass);
		matRock.setFloat("DiffuseMap_0_scale", 64);
		Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
		dirt.setWrap(WrapMode.Repeat);
		matRock.setTexture("DiffuseMap_1", dirt);
		matRock.setFloat("DiffuseMap_1_scale", 16);
		Texture rock = assetManager.loadTexture("Textures/Terrain/splat/road.jpg");
		rock.setWrap(WrapMode.Repeat);
		matRock.setTexture("DiffuseMap_2", rock);
		matRock.setFloat("DiffuseMap_2_scale", 128);
		Texture normalMap0 = assetManager.loadTexture("Textures/Terrain/splat/grass_normal.jpg");
		normalMap0.setWrap(WrapMode.Repeat);
		Texture normalMap1 = assetManager.loadTexture("Textures/Terrain/splat/dirt_normal.png");
		normalMap1.setWrap(WrapMode.Repeat);
		Texture normalMap2 = assetManager.loadTexture("Textures/Terrain/splat/road_normal.png");
		normalMap2.setWrap(WrapMode.Repeat);
		matRock.setTexture("NormalMap", normalMap0);
		matRock.setTexture("NormalMap_1", normalMap2);
		matRock.setTexture("NormalMap_2", normalMap2);

		AbstractHeightMap heightmap = null;
		try {
			heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 0.25f);
			heightmap.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
		List<Camera> cameras = new ArrayList<Camera>();
		cameras.add(getCamera());
		terrain.setMaterial(matRock);
		terrain.setLocalScale(new Vector3f(5, 5, 5));
		terrain.setLocalTranslation(new Vector3f(0, -30, 0));
		terrain.setLocked(false); // unlock it so we can edit the height

		RigidBodyControl rbc = new RigidBodyControl(0);
		terrain.addControl(rbc);
		phsyicsState.getPhysicsSpace().add(rbc);

		terrain.setShadowMode(ShadowMode.Receive);
		rootNode.attachChild(terrain);

		// TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
		// terrain.addControl(control);
	}

	Quaternion carrierCurrentRotation = new Quaternion();
	final static int maxDeltaWaterHeight = 20;

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);
		time += tpf;
		waterHeight = (float) Math.cos(((time * 0.3f) % FastMath.TWO_PI)) * 1.4f + initialWaterHeight;

		if (water != null)
			water.setWaterHeight(waterHeight);
	}
}
