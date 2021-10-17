package net.carriercommander;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.*;
import com.jme3.input.event.*;
import com.jme3.math.*;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.water.WaterFilter;
import net.carriercommander.control.*;
import net.carriercommander.objects.*;
import net.carriercommander.shared.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PlayerAppState extends AbstractAppState {
	Logger logger = LoggerFactory.getLogger(PlayerAppState.class);

	private final RenderManager renderManager;
	private final BulletAppState physicsState;
	private final Node rootNode;
	private final PlayerData playerData;
	private final CameraNode camNode;
	private final WaterFilter water;
	private InputManager inputManager;
	private AssetManager assetManager;

	private boolean mouseGrabbed = false;
	private Geometry mark;
	private PlayerItem activeUnit;
	private Carrier carrier;
	private ShipControl carrierControl;
	private final List<Walrus> walrus = new ArrayList<>();
	private final List<ShipControl> walrusControl = new ArrayList<>();
	private final List<Manta> manta = new ArrayList<>();
	private final List<PlaneControl> mantaControl = new ArrayList<>();

	PlayerAppState(RenderManager renderManager, BulletAppState physicsState, Node rootNode, PlayerData playerData, CameraNode camNode, WaterFilter water) {
		this.renderManager = renderManager;
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

		activeUnit = (PlayerItem) rootNode.getChild(Constants.CARRIER_PLAYER);
	}

	private void initPlayer() {
		createCarrier();
		createWalrus();
		createManta();
	}

	private void createCarrier() {
		carrier = new Carrier(Constants.CARRIER_PLAYER, assetManager, physicsState, water, camNode);
		carrierControl = carrier.getControl(ShipControl.class);
		carrierControl.setPhysicsLocation(new Vector3f(300, (water != null ? water.getWaterHeight() : 0), 1700));
		rootNode.attachChild(carrier);
	}

	private void createWalrus() {
		Walrus unit = new Walrus(Constants.WALRUS_1, assetManager, physicsState, water, camNode);
		walrusControl.add(unit.getControl(ShipControl.class));
		unit.getControl(VehicleControl.class).setPhysicsLocation(new Vector3f(850, 5, 1700));
		rootNode.attachChild(unit);
		walrus.add(unit);

		unit = new Walrus(Constants.WALRUS_2, assetManager, physicsState, water, camNode);
		walrusControl.add(unit.getControl(ShipControl.class));
		unit.getControl(VehicleControl.class).setPhysicsLocation(new Vector3f(550, 0, 1700));
		rootNode.attachChild(unit);
		walrus.add(unit);

		unit = new Walrus(Constants.WALRUS_3, assetManager, physicsState, water, camNode);
		walrusControl.add(unit.getControl(ShipControl.class));
		unit.getControl(VehicleControl.class).setPhysicsLocation(new Vector3f(500, 0, 1700));
		rootNode.attachChild(unit);
		walrus.add(unit);

		unit = new Walrus(Constants.WALRUS_4, assetManager, physicsState, water, camNode);
		walrusControl.add(unit.getControl(ShipControl.class));
		unit.getControl(VehicleControl.class).setPhysicsLocation(new Vector3f(450, 0, 1700));
		rootNode.attachChild(unit);
		walrus.add(unit);
	}

	private void createManta() {
		Manta unit = new Manta(Constants.MANTA_1, assetManager, physicsState, camNode);
		mantaControl.add(unit.getControl(PlaneControl.class));
//		mantaControl.get(0).setPhysicsLocation(new Vector3f(-1450, 20, 400));
		mantaControl.get(0).setPhysicsLocation(new Vector3f(850, (water != null ? water.getWaterHeight() : 0) + 15, 1700));
		rootNode.attachChild(unit);
		manta.add(unit);

		unit = new Manta(Constants.MANTA_2, assetManager, physicsState, camNode);
		mantaControl.add(unit.getControl(PlaneControl.class));
		mantaControl.get(1).setPhysicsLocation(new Vector3f(550, 20, 1600));
		rootNode.attachChild(unit);
		manta.add(unit);

		unit = new Manta(Constants.MANTA_3, assetManager, physicsState, camNode);
		mantaControl.add(unit.getControl(PlaneControl.class));
		mantaControl.get(2).setPhysicsLocation(new Vector3f(500, 20, 1600));
		rootNode.attachChild(unit);
		manta.add(unit);

		unit = new Manta(Constants.MANTA_4, assetManager, physicsState, camNode);
		mantaControl.add(unit.getControl(PlaneControl.class));
		mantaControl.get(3).setPhysicsLocation(new Vector3f(450, 20, 1600));
		rootNode.attachChild(unit);
		manta.add(unit);
	}

	private void initInput() {
		inputManager.addMapping(Constants.INPUT_LEFT, new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping(Constants.INPUT_RIGHT, new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping(Constants.INPUT_UP, new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping(Constants.INPUT_DOWN, new KeyTrigger(KeyInput.KEY_DOWN));
		inputManager.addMapping(Constants.INPUT_ACCELERATE, new KeyTrigger(KeyInput.KEY_PGUP), new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		inputManager.addMapping(Constants.INPUT_DECELERATE, new KeyTrigger(KeyInput.KEY_PGDN), new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
		inputManager.addMapping(Constants.INPUT_FIRE, new KeyTrigger(KeyInput.KEY_SPACE), new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping(Constants.INPUT_GRAB_MOUSE, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
		inputManager.addMapping(Constants.INPUT_MOUSE_X, new MouseAxisTrigger(MouseInput.AXIS_X, false));
		inputManager.addMapping(Constants.INPUT_MOUSE_Y, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
		inputManager.addMapping(Constants.INPUT_MOUSE_X_NEGATIVE, new MouseAxisTrigger(MouseInput.AXIS_X, true));
		inputManager.addMapping(Constants.INPUT_MOUSE_Y_NEGATIVE, new MouseAxisTrigger(MouseInput.AXIS_Y, true));

		inputManager.addListener((AnalogListener) (name, value, tpf) -> {
					switch (name) {
						case Constants.INPUT_LEFT:
							getActiveUnitControl().steerLeft(tpf);
							break;
						case Constants.INPUT_RIGHT:
							getActiveUnitControl().steerRight(tpf);
							break;
						case Constants.INPUT_UP:
							getActiveUnitControl().steerDown(tpf);
							break;
						case Constants.INPUT_DOWN:
							getActiveUnitControl().steerUp(tpf);
							break;
						case Constants.INPUT_ACCELERATE:
							getActiveUnitControl().increaseSpeed(tpf);
							break;
						case Constants.INPUT_DECELERATE:
							getActiveUnitControl().decreaseSpeed(tpf);
							break;
						case Constants.INPUT_MOUSE_X_NEGATIVE:
							value = -value;
						case Constants.INPUT_MOUSE_X:
							if (mouseGrabbed) {
								getActiveUnitControl().setRudder(getActiveUnitControl().getRudder() - value);
							}
							break;
						case Constants.INPUT_MOUSE_Y_NEGATIVE:
							value = -value;
						case Constants.INPUT_MOUSE_Y:
							if (mouseGrabbed) {
								getActiveUnitControl().setAttitude(getActiveUnitControl().getAttitude() + value);
							}
							break;
					}
					VehicleControl vehicleControl = activeUnit.getControl(VehicleControl.class);
					if (vehicleControl  != null) {
						vehicleControl.steer(getActiveUnitControl().getRudder());
						vehicleControl.accelerate(getActiveUnitControl().getThrottle() * vehicleControl.getMass());
					}
				}, Constants.INPUT_LEFT, Constants.INPUT_RIGHT, Constants.INPUT_UP, Constants.INPUT_DOWN,
				Constants.INPUT_ACCELERATE, Constants.INPUT_DECELERATE, Constants.INPUT_MOUSE_X,
				Constants.INPUT_MOUSE_Y, Constants.INPUT_MOUSE_X_NEGATIVE, Constants.INPUT_MOUSE_Y_NEGATIVE);
		inputManager.addListener((ActionListener) (name, value, tpf) -> {
			switch (name) {
				case Constants.INPUT_FIRE:
					if (value) {
						fire();
					}
					break;
				case Constants.INPUT_GRAB_MOUSE:
					if (value) {
						mouseGrabbed = !mouseGrabbed;
						inputManager.setCursorVisible(!mouseGrabbed);
					}
					break;
			}
		}, Constants.INPUT_FIRE, Constants.INPUT_GRAB_MOUSE);
/*
		inputManager.addRawInputListener(new RawInputListener() {
			public void onJoyAxisEvent(JoyAxisEvent evt) {
			}

			public void onJoyButtonEvent(JoyButtonEvent evt) {
			}

			public void onMouseMotionEvent(MouseMotionEvent evt) {
				 logger.debug("Mouse moved x:{} y:{}", evt.getX(), evt.getY());
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
		});*/
	}

	public void setActiveUnit(PlayerItem unit) {
		activeUnit = unit;
	}

	public PlayerItem getActiveUnit() {
		return activeUnit;
	}

	private PlayerControl getActiveUnitControl() {
		return activeUnit.getControl(PlayerControl.class);
	}

	public void fire() {
		switch (getActiveUnitControl().getWeaponType()) {
			case CANON:
				fireCanon();
				break;
			case LASER:
				break;
			case MISSILE:
				fireMissile();
				break;
			case BOMB:
				break;
			case POD:
				break;
		}
	}

	private void fireCanon() {
		Quaternion rotation = getActiveUnitControl().getPhysicsRotation();
		Projectile projectile = new Projectile("projectile" + System.currentTimeMillis(), assetManager,  physicsState.getPhysicsSpace(), rotation);
		ProjectileControl control = projectile.getControl(ProjectileControl.class);
		control.setPhysicsLocation(getActiveUnitControl().getPhysicsLocation().add(rotation.mult(Vector3f.UNIT_Z).mult(15)));
		control.setPhysicsRotation(rotation);
		rootNode.attachChild(projectile);
	}

	private void fireMissile() {
		GameItem target = findTarget(camNode.getWorldTranslation(), camNode.getWorldRotation(), rootNode);
		if (target != null) {
			logger.info("fire missile at {}", target.getName());
			Missile missile = new Missile(Constants.MISSILE + System.currentTimeMillis(), assetManager, physicsState.getPhysicsSpace(), renderManager, rootNode, target);
			MissileControl control = missile.getControl(MissileControl.class);
			Vector3f location = control.getPhysicsLocation();
			location.y -= 5; // TODO respect the attitude and angle
			control.setPhysicsRotation(control.getPhysicsRotation().mult(new Quaternion().fromAngles(0, FastMath.PI, 0)));
			rootNode.attachChild(missile);
//			missile.setCameraToFront();
		}
	}

	private GameItem findTarget(Vector3f translation, Quaternion rotation, Node rootNode) {
		CollisionResults results = new CollisionResults();
		Ray ray = new Ray(translation, rotation.mult(Vector3f.UNIT_Z));
		rootNode.collideWith(ray, results);

		if (results.size() > 0 && results.getClosestCollision().getGeometry() != null) {
			Node node = results.getClosestCollision().getGeometry().getParent();
			while (node != null) {
				if (node instanceof GameItem) {
					return (GameItem) node;
				}
				node = node.getParent();
			}
		}
		return null;
	}

	@Override
	public void update(float tpf) {
		//TODO check if we're connected
/*		CarrierData carrierData = playerData.getCarrier();
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
		}*/
	}
}
