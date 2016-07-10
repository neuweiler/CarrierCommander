package net.carriercommander.network;

import java.io.IOException;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Network;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.system.JmeContext;
import com.jme3.util.SkyFactory;

import net.carriercommander.shared.Utils;
import net.carriercommander.shared.messages.PlayerDataMessage;
import net.carriercommander.shared.messages.TextMessage;
import net.carriercommander.shared.model.PlayerData;

public class ClientMain extends SimpleApplication implements ClientStateListener {

	private Client client;
	private PlayerAppState playerAppState;
	private BulletAppState bulletAppState;
	private PlayerData playerData = new PlayerData();
	private SceneManager sceneManager = new SceneManager(this);
	private CameraNode camNode;
	private Vector3f lightDir = new Vector3f(-4.9236743f, -1.27054665f, 5.896916f);
	private String hostAddress = "127.0.0.1";
	private int hostPort = 6000;

	public static void main(String[] args) {
		Utils.initSerializers();

		ClientMain app = new ClientMain();
		if (args.length > 0) {
			app.hostAddress = args[0];
		}
		if (args.length > 1) {
			app.hostPort = Integer.parseInt(args[1]);
		}

		app.setPauseOnLostFocus(false);
		app.start(JmeContext.Type.Display);
	}

	@Override
	public void simpleInitApp() {
		initConnection();
		initPhysics();
		initCam();
		initAppStates();
		initScene();
		initCrossHairs();
	}

	private void initConnection() {
		try {
			System.out.print("connecting to " + hostAddress + ", port " + hostPort + "...");
			client = Network.connectToServer(hostAddress, hostPort);
			System.out.println("connected !");
			client.addMessageListener(new ClientListener(client, sceneManager));
			client.addClientStateListener(this);
			client.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initPhysics() {
		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);

		// bulletAppState.setDebugEnabled(true);
	}

	private void initCam() {
		// setDisplayFps(false);
		setDisplayStatView(false);
		flyCam.setEnabled(false); // Disable the default flyby cam
		// flyCam.setMoveSpeed(100);
		// flyCam.setDragToRotate(true);
		// cam.setLocation(new Vector3f(0, 5, 15));

		camNode = new CameraNode("Camera Node", cam);
		camNode.setControlDir(ControlDirection.SpatialToCamera);
		camNode.setLocalTranslation(0, 3, 0);

		// cam.setLocation(new Vector3f(-600, 130, 200));
		// cam.setRotation(new Quaternion().fromAngles(new float[] {
		// FastMath.DEG_TO_RAD * 0f, FastMath.DEG_TO_RAD * 0f, 0 }));

		cam.setFrustumFar(4000);

	}

	private void initAppStates() {
		playerAppState = new PlayerAppState(bulletAppState, rootNode, playerData, camNode);
		stateManager.attach(playerAppState);
	}

	private void initScene() {
		viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));

		Spatial sceneModel = assetManager.loadModel("Scenes/town/main.j3o");
		sceneModel.scale(2f);
		CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(sceneModel);
		RigidBodyControl landscape = new RigidBodyControl(sceneShape, 0);
		sceneModel.addControl(landscape);
		bulletAppState.getPhysicsSpace().add(landscape);
		rootNode.attachChild(sceneModel);

		Spatial sky = SkyFactory.createSky(assetManager, "Scenes/Beach/FullskiesSunset0068.dds", false);
		sky.setLocalScale(350);
		rootNode.attachChild(sky);

		AmbientLight ambient = new AmbientLight();
		ambient.setColor(ColorRGBA.White);
		rootNode.addLight(ambient);

		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(lightDir);
		sun.setColor(ColorRGBA.White.clone().multLocal(0.7f));
		rootNode.addLight(sun);

		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
		LightScatteringFilter lsf = new LightScatteringFilter(lightDir.mult(-300));
		lsf.setLightDensity(1.0f);
		fpp.addFilter(lsf);
		viewPort.addProcessor(fpp);
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);

		if (client != null && client.isConnected() && playerData != null /*&& playerData.isDirty()*/) {
			client.send(new PlayerDataMessage(playerData));
			playerData.clean();
		}
	}

	@Override
	public void clientConnected(Client c) {
		playerData.setId(c.getId());
		sceneManager.setMyId(c.getId());
		client.send(new TextMessage("Hello Server! I'm ID" + c.getId()));
	}

	@Override
	public void clientDisconnected(Client arg0, DisconnectInfo arg1) {
		// TODO Auto-generated method stub

	}

	protected void initCrossHairs() {
		setDisplayStatView(false);
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		BitmapText ch = new BitmapText(guiFont, false);
		ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
		ch.setText("+"); // crosshairs
		ch.setColor(ColorRGBA.Red);
		ch.setLocalTranslation( // center
				settings.getWidth() / 2 - ch.getLineWidth()/2, settings.getHeight() / 2 + ch.getLineHeight()/2, 0);
		guiNode.attachChild(ch);
	}
}
