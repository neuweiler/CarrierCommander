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

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace.BroadphaseType;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Network;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.DepthOfFieldFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.post.filters.TranslucentBucketFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
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
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.tools.SizeValue;
import net.carriercommander.Constants.GameType;
import net.carriercommander.network.ClientListener;
import net.carriercommander.network.SceneManager;
import net.carriercommander.objects.IslandMap;
import net.carriercommander.screen.HudScreenControl;
import net.carriercommander.screen.StartScreenControl;
import net.carriercommander.shared.Utils;
import net.carriercommander.shared.messages.PlayerDataMessage;
import net.carriercommander.shared.messages.TextMessage;
import net.carriercommander.shared.model.PlayerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Carrier Commander Main Class
 *
 * @author Michael Neuweiler
 */
public class CarrierCommander extends SimpleApplication implements ClientStateListener {
	Logger logger = LoggerFactory.getLogger(CarrierCommander.class);

	private PlayerAppState playerAppState;
	private BulletAppState physicsState;
	private Nifty nifty;
	private final Vector3f lightDir = new Vector3f(-4.9236743f, -1.27054665f, 5.896916f);
	private WaterFilter water = null;
	private CameraNode camNode = null;

	private float time = 0.0f;
	private final float initialWaterHeight = 0f;
	private boolean loading = false;
	private int loadPart = 0;
	private GameType gameType;
	private final String hostAddress = "127.0.0.1";
	private final int hostPort = 6000;
	private Client networkClient;
	private final PlayerData playerData = new PlayerData();
	private SceneManager sceneManager = new SceneManager(this);

	public static void main(String[] args) {
		Utils.initSerializers();
		CarrierCommander app = new CarrierCommander();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		// setDisplayFps(false);
		setDisplayStatView(false);
		flyCam.setEnabled(false); // Disable the default flyby cam
		createNitfyGui();
	}

	public void startGame(String gameType) {
		if ("network".equals(gameType)) {
			this.gameType = GameType.network;
		} else if ("action".equals(gameType)) {
			this.gameType = GameType.action;
		} else {
			this.gameType = GameType.strategy;
		}

		loading = true;
		loadPart = 0;
	}

	private void load() {
		switch (loadPart) {
			case 0:
				if (gameType == GameType.network) {
					setProgress(0.0f, "connecting to server");
					connectToServer();
				}
				break;
			case 1:
				setProgress(0.1f, "activate physics");
				activatePhysics();
				break;
			case 2:
				setProgress(0.2f, "configure camera");
				configureCamera();
				break;
			case 3:
				setProgress(0.3f, "creating terrain");
				createTerrain();
				break;
			case 4:
				setProgress(0.4f, "creating sky");
				createSky();
				break;
			case 5:
				setProgress(0.5f, "creating water");
				createWater();
				break;
			case 6:
				setProgress(0.6f, "adding post process filter");
				addPostProcessFilter();
				break;
			case 7:
				setProgress(0.7f, "creating objects");
				playerAppState = new PlayerAppState(physicsState, rootNode, playerData, camNode, water);
				stateManager.attach(playerAppState);
				break;
			case 10:
				setProgress(1.0f, "finished");
				nifty.gotoScreen("hud");
				loading = false;
				break;
		}
		loadPart++;
	}

	private void setProgress(final float progress, String loadingText) {
		final int MIN_WIDTH = 32;

		Element progressBarElement = nifty.getScreen("load").findElementById("progressbar");
		int pixelWidth = (int) (MIN_WIDTH + (progressBarElement.getParent().getWidth() - MIN_WIDTH) * progress);
		progressBarElement.setConstraintWidth(new SizeValue(pixelWidth + "px"));
		progressBarElement.getParent().layoutElements();

		TextRenderer textRenderer = nifty.getScreen("load").findElementById("loadingtext").getRenderer(TextRenderer.class);
		if (textRenderer != null) {
			textRenderer.setText(loadingText);
		}
	}

	private void createNitfyGui() {
		NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, viewPort);
		nifty = niftyDisplay.getNifty();

		nifty.addXml("Interface/Screens/start.xml");
		nifty.addXml("Interface/Screens/load.xml");
		nifty.addXml("Interface/Screens/hud.xml");
		nifty.gotoScreen("start");

		StartScreenControl startScreenControl = (StartScreenControl) nifty.getScreen(Constants.SCREEN_START).getScreenController();
		HudScreenControl hudScreenControl = (HudScreenControl) nifty.getScreen(Constants.SCREEN_HUD).getScreenController();
		hudScreenControl.attachScene(rootNode);

		stateManager.attach(startScreenControl);
		stateManager.attach(hudScreenControl);
		guiViewPort.addProcessor(niftyDisplay);
	}

	private void connectToServer() {
		setPauseOnLostFocus(false);

		try {
			sceneManager = new SceneManager(this);
			logger.info("connecting to {}, port {}", hostAddress, hostPort);
			networkClient = Network.connectToServer(hostAddress, hostPort);
			logger.info("connected !");
			networkClient.addMessageListener(new ClientListener(networkClient, sceneManager));
			networkClient.addClientStateListener(this);
			networkClient.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void activatePhysics() {
		physicsState = new BulletAppState();
		physicsState.setThreadingType(BulletAppState.ThreadingType.PARALLEL); // do not set while enabling debug !
		physicsState.setBroadphaseType(BroadphaseType.SIMPLE);
		stateManager.attach(physicsState);

//		physicsState.setDebugEnabled(true);
	}


	private void addPostProcessFilter() {
		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);

		if (water != null) {
			fpp.addFilter(water);
		}

		BloomFilter bloom = new BloomFilter();
		bloom.setExposurePower(35);
		bloom.setBloomIntensity(.5f);
		fpp.addFilter(bloom);

		LightScatteringFilter lsf = new LightScatteringFilter(lightDir.mult(-300));
		lsf.setLightDensity(1.0f);
		fpp.addFilter(lsf);

/*		DepthOfFieldFilter dof = new DepthOfFieldFilter();
		dof.setFocusDistance(1000);
		dof.setFocusRange(1000);
		fpp.addFilter(dof);
*/
		fpp.addFilter(new TranslucentBucketFilter());
		fpp.setNumSamples(4);

		viewPort.addProcessor(fpp);
	}

	private void createWater() {
		water = new WaterFilter(rootNode, lightDir);

		water.setWaveScale(0.003f);
		water.setMaxAmplitude(2f);
		water.setFoamExistence(new Vector3f(1f, 4, 0.5f));
		water.setFoamTexture((Texture2D) assetManager.loadTexture("Common/MatDefs/Water/Textures/foam2.jpg"));
		//water.setUseHQShoreline(true);
		// water.setNormalScale(0.5f);
		// water.setRefractionConstant(0.25f);
		// water.setRefractionStrength(0.2f);
		// water.setFoamHardness(0.6f);
		water.setWaterHeight(initialWaterHeight);
	}

	private void createSky() {
		Spatial sky = SkyFactory.createSky(assetManager, "Scenes/Beach/FullskiesSunset0068.dds", SkyFactory.EnvMapType.CubeMap);

		sky.setLocalScale(350);
		rootNode.attachChild(sky);

		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(lightDir);
		sun.setColor(ColorRGBA.White.clone().multLocal(1.7f));
		rootNode.addLight(sun);
	}

	private void configureCamera() {
		camNode = new CameraNode("Camera Node", cam);
		camNode.setControlDir(ControlDirection.SpatialToCamera);

		cam.setFrustumFar(20000);
	}

	private void createTerrain() {
		Material matRock = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
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

		final AbstractHeightMap heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 0.25f);
		heightmap.load();

		IslandMap.getInstance().getIslands().stream().forEach(island -> {
			logger.debug("creating island {}", island.getName());
			TerrainQuad terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
			terrain.setName(island.getName());
			terrain.setMaterial(matRock);
			terrain.setLocalScale(new Vector3f(5, 5, 5));
			terrain.setLocalTranslation(island.getPosition());
			terrain.setLocked(false); // unlock it so we can edit the height

			RigidBodyControl rbc = new RigidBodyControl(0);
			terrain.addControl(rbc);
			physicsState.getPhysicsSpace().add(rbc);

			terrain.setShadowMode(ShadowMode.Receive);
			rootNode.attachChild(terrain);
		});

		// TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
		// terrain.addControl(control);
	}

	@Override
	public void clientConnected(Client c) {
		playerData.setId(c.getId());
		sceneManager.setMyId(c.getId());
		networkClient.send(new TextMessage("Hello Server! I'm ID" + c.getId()));
	}

	@Override
	public void clientDisconnected(Client arg0, DisconnectInfo arg1) {
		logger.warn("client disconnected!");
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);

		if (loading) {
			load();
		}

		sendPlayerData();
		updateWaterHeight(tpf);
	}

	private void updateWaterHeight(float tpf) {
		time += tpf;
		if (water != null) {
			float waterHeight = (float) Math.cos(((time * 0.3f) % FastMath.TWO_PI)) * 1.4f + initialWaterHeight;
			water.setWaterHeight(waterHeight);
		}
	}

	private void sendPlayerData() {
		if (networkClient != null && networkClient.isConnected() && playerData.isModified()) {
			networkClient.send(new PlayerDataMessage(playerData));
			playerData.clean();
		}
	}
}
