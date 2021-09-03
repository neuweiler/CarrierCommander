package net.carriercommander;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.event.*;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.water.WaterFilter;
import net.carriercommander.control.PlaneControl;
import net.carriercommander.control.ShipControl;
import net.carriercommander.objects.Carrier;
import net.carriercommander.objects.Manta;
import net.carriercommander.objects.PlayerUnit;
import net.carriercommander.objects.Walrus;
import net.carriercommander.shared.model.CarrierData;
import net.carriercommander.shared.model.MantaData;
import net.carriercommander.shared.model.PlayerData;
import net.carriercommander.shared.model.WalrusData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PlayerAppState extends AbstractAppState {
	Logger logger = LoggerFactory.getLogger(PlayerAppState.class);

	private final BulletAppState physicsState;
	private final Node rootNode;
	private final PlayerData playerData;
	private final CameraNode camNode;
	private final WaterFilter water;
	private InputManager inputManager;
	private AssetManager assetManager;

	private Geometry mark;
	private PlayerUnit activeUnit;
	private Carrier carrier;
	private ShipControl carrierControl;
	private final List<Walrus> walrus = new ArrayList<>();
	private final List<ShipControl> walrusControl = new ArrayList<>();
	private final List<Manta> manta = new ArrayList<>();
	private final List<PlaneControl> mantaControl = new ArrayList<>();

	PlayerAppState(BulletAppState physicsState, Node rootNode, PlayerData playerData, CameraNode camNode, WaterFilter water) {
		this.physicsState = physicsState;
		this.playerData = playerData;
		this.rootNode = rootNode;
		this.camNode = camNode;
		this.water = water;
	}

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.inputManager = app.getInputManager();
		this.assetManager = app.getAssetManager();

		initPlayer();
		initInput();
		initMark();

		activeUnit = (PlayerUnit) rootNode.getChild(Constants.CARRIER_PLAYER);
		// carrier.setCameraToBridge(camNode);
		// camNode.lookAt(target.getLocalTranslation(), Vector3f.UNIT_Y);
	}

	private void initPlayer() {
		createCarrier();
		createWalrus();
		createManta();
	}

	private void createCarrier() {
		carrier = new Carrier(Constants.CARRIER_PLAYER, assetManager, physicsState, water, camNode);
		carrierControl = carrier.getControl(ShipControl.class);
		carrierControl.setPhysicsLocation(new Vector3f(-1800, (water != null ? water.getWaterHeight() : 0) + 5, 800));
		rootNode.attachChild(carrier);
		//		bulletAppState.getPhysicsSpace().addAll(player);
	}

	private void createWalrus() {
		Walrus unit = new Walrus(Constants.WALRUS_1, assetManager, physicsState, water, camNode);
		walrusControl.add(unit.getControl(ShipControl.class));
		walrusControl.get(0).setPhysicsLocation(new Vector3f(-1450,  0, 500));
		rootNode.attachChild(unit);
		walrus.add(unit);

		unit = new Walrus(Constants.WALRUS_2, assetManager, physicsState, water, camNode);
		walrusControl.add(unit.getControl(ShipControl.class));
		walrusControl.get(1).setPhysicsLocation(new Vector3f(-1400, 0, 500));
		rootNode.attachChild(unit);
		walrus.add(unit);

		unit = new Walrus(Constants.WALRUS_3, assetManager, physicsState, water, camNode);
		walrusControl.add(unit.getControl(ShipControl.class));
		walrusControl.get(2).setPhysicsLocation(new Vector3f(-1350, 0, 500));
		rootNode.attachChild(unit);
		walrus.add(unit);

		unit = new Walrus(Constants.WALRUS_4, assetManager, physicsState, water, camNode);
		walrusControl.add(unit.getControl(ShipControl.class));
		walrusControl.get(3).setPhysicsLocation(new Vector3f(-1300, 0, 500));
		rootNode.attachChild(unit);
		walrus.add(unit);
	}

	private void createManta() {
		Manta unit = new Manta(Constants.MANTA_1, assetManager, physicsState, water, camNode);
		mantaControl.add(unit.getControl(PlaneControl.class));
		mantaControl.get(0).setPhysicsLocation(new Vector3f(-1450, 20, 400));
		rootNode.attachChild(unit);
		manta.add(unit);

		unit = new Manta(Constants.MANTA_2, assetManager, physicsState, water, camNode);
		mantaControl.add(unit.getControl(PlaneControl.class));
		mantaControl.get(1).setPhysicsLocation(new Vector3f(-1400, 20, 400));
		rootNode.attachChild(unit);
		manta.add(unit);

		unit = new Manta(Constants.MANTA_3, assetManager, physicsState, water, camNode);
		mantaControl.add(unit.getControl(PlaneControl.class));
		mantaControl.get(2).setPhysicsLocation(new Vector3f(-1350, 20, 400));
		rootNode.attachChild(unit);
		manta.add(unit);

		unit = new Manta(Constants.MANTA_4, assetManager, physicsState, water, camNode);
		mantaControl.add(unit.getControl(PlaneControl.class));
		mantaControl.get(3).setPhysicsLocation(new Vector3f(-1300, 20, 400));
		rootNode.attachChild(unit);
		manta.add(unit);
	}

	private void initInput() {
		inputManager.addMapping(Constants.INPUT_LEFT, new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping(Constants.INPUT_RIGHT, new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping(Constants.INPUT_UP, new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping(Constants.INPUT_DOWN, new KeyTrigger(KeyInput.KEY_DOWN));
		inputManager.addMapping(Constants.INPUT_ACCELERATE, new KeyTrigger(KeyInput.KEY_PGUP),
				new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
		inputManager.addMapping(Constants.INPUT_DECELERATE, new KeyTrigger(KeyInput.KEY_PGDN),
				new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		inputManager.addListener((AnalogListener) (name, value, tpf) -> {
					switch (name) {
						case Constants.INPUT_LEFT:
							activeUnit.steerLeft(tpf);
							break;
						case Constants.INPUT_RIGHT:
							activeUnit.steerRight(tpf);
							break;
						case Constants.INPUT_UP:
							activeUnit.steerUp(tpf);
							break;
						case Constants.INPUT_DOWN:
							activeUnit.steerDown(tpf);
							break;
						case Constants.INPUT_ACCELERATE:
							activeUnit.increaseSpeed(tpf);
							break;
						case Constants.INPUT_DECELERATE:
							activeUnit.decreaseSpeed(tpf);
							break;
					}
				}, Constants.INPUT_LEFT, Constants.INPUT_RIGHT, Constants.INPUT_UP, Constants.INPUT_DOWN,
				Constants.INPUT_ACCELERATE, Constants.INPUT_DECELERATE);

		inputManager.addRawInputListener(new RawInputListener() {
			public void onJoyAxisEvent(JoyAxisEvent evt) {
			}

			public void onJoyButtonEvent(JoyButtonEvent evt) {
			}

			public void onMouseMotionEvent(MouseMotionEvent evt) {
				// logger.debug("Mouse moved x:{} y:{}", evt.getX(), evt.getY());
			}

			public void onMouseButtonEvent(MouseButtonEvent evt) {
			}

			public void onKeyEvent(KeyInputEvent evt) {
			}

			public void beginInput() {
			}

			public void endInput() {
			}

			public void onTouchEvent(TouchEvent evt) {
			}
		});
	}

	public void setActiveUnit(PlayerUnit unit) {
		activeUnit = unit;
	}

	public PlayerUnit getActiveUnit() {
		return activeUnit;
	}

	@Override
	public void update(float tpf) {
		CarrierData carrierData = playerData.getCarrier();
		carrierData.setLocation(carrierControl.getPhysicsLocation());
		carrierData.setRotation(carrierControl.getPhysicsRotation());
		carrierData.setVelocity(carrierControl.getLinearVelocity());

		for (int i = 0; i < 4; i++) {
			MantaData mantaData = playerData.getManta(i);
			mantaData.setLocation(mantaControl.get(i).getPhysicsLocation());
			mantaData.setRotation(mantaControl.get(i).getPhysicsRotation());
			mantaData.setVelocity(mantaControl.get(i).getLinearVelocity());

			WalrusData walrusData = playerData.getWalrus(i);
			walrusData.setLocation(walrusControl.get(i).getPhysicsLocation());
			walrusData.setRotation(walrusControl.get(i).getPhysicsRotation());
			walrusData.setVelocity(walrusControl.get(i).getLinearVelocity());
		}
	}

	private void initMark() {
		Sphere sphere = new Sphere(30, 30, 0.2f);
		mark = new Geometry("BOOM!", sphere);
		Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mark_mat.setColor("Color", ColorRGBA.Red);
		mark.setMaterial(mark_mat);
	}
}
