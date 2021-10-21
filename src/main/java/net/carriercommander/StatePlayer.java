package net.carriercommander;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.water.WaterFilter;
import net.carriercommander.control.*;
import net.carriercommander.network.model.PlayerData;
import net.carriercommander.objects.*;
import net.carriercommander.ui.AbstractState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class StatePlayer extends AbstractState {
	Logger logger = LoggerFactory.getLogger(StatePlayer.class);

	public enum PlayerUnit { CARRIER, WALRUS, MANTA }

	private final PlayerData playerData = new PlayerData();
	private final CameraNode camNode;
	private WaterFilter water;
	private InputManager inputManager;
	private AssetManager assetManager;
	private BulletAppState physicsState;

	private boolean mouseGrabbed = false;
	private PlayerItem activeUnit;
	private Carrier carrier;
	private final List<Walrus> walrus = new ArrayList<>();
	private final List<Manta> manta = new ArrayList<>();

	public StatePlayer(CameraNode camNode) {
		this.camNode = camNode;
	}

	@Override
	protected void initialize(Application app) {
		inputManager = app.getInputManager();
		assetManager = app.getAssetManager();
		water = getState(StateWater.class).getWater();
		physicsState = getState(BulletAppState.class);

		initPlayer();
		initInput();
	}

	@Override
	protected void cleanup(Application app) {

	}

	@Override
	protected void onEnable() {
		activeUnit = (PlayerItem) getRootNode().getChild(Constants.CARRIER_PLAYER);
		activeUnit.setCameraToFront();
	}

	@Override
	protected void onDisable() {
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

	private void initPlayer() {
		createCarrier();
		createWalrus();
		createManta();
	}

	private void createCarrier() {
		carrier = new Carrier(Constants.CARRIER_PLAYER, assetManager, physicsState, water, camNode);
		ShipControl carrierControl = carrier.getControl(ShipControl.class);
		carrierControl.setPhysicsLocation(new Vector3f(300, (water != null ? water.getWaterHeight() : 0), 1700));
		getRootNode().attachChild(carrier);
	}

	private void createWalrus() {
		Walrus unit = new Walrus(Constants.WALRUS_1, assetManager, physicsState, water, camNode);
		unit.getControl(VehicleControl.class).setPhysicsLocation(new Vector3f(850, 5, 1700));
		getRootNode().attachChild(unit);
		walrus.add(unit);

		unit = new Walrus(Constants.WALRUS_2, assetManager, physicsState, water, camNode);
		unit.getControl(VehicleControl.class).setPhysicsLocation(new Vector3f(550, 0, 1700));
		getRootNode().attachChild(unit);
		walrus.add(unit);

		unit = new Walrus(Constants.WALRUS_3, assetManager, physicsState, water, camNode);
		unit.getControl(VehicleControl.class).setPhysicsLocation(new Vector3f(500, 0, 1700));
		getRootNode().attachChild(unit);
		walrus.add(unit);

		unit = new Walrus(Constants.WALRUS_4, assetManager, physicsState, water, camNode);
		unit.getControl(VehicleControl.class).setPhysicsLocation(new Vector3f(450, 0, 1700));
		getRootNode().attachChild(unit);
		walrus.add(unit);
	}

	private void createManta() {
		Manta unit = new Manta(Constants.MANTA_1, assetManager, physicsState, camNode);
		unit.getControl(PlaneControl.class).setPhysicsLocation(new Vector3f(850, (water != null ? water.getWaterHeight() : 0) + 15, 1700));
		getRootNode().attachChild(unit);
		manta.add(unit);

		unit = new Manta(Constants.MANTA_2, assetManager, physicsState, camNode);
		unit.getControl(PlaneControl.class).setPhysicsLocation(new Vector3f(550, 20, 1600));
		getRootNode().attachChild(unit);
		manta.add(unit);

		unit = new Manta(Constants.MANTA_3, assetManager, physicsState, camNode);
		unit.getControl(PlaneControl.class).setPhysicsLocation(new Vector3f(500, 20, 1600));
		getRootNode().attachChild(unit);
		manta.add(unit);

		unit = new Manta(Constants.MANTA_4, assetManager, physicsState, camNode);
		unit.getControl(PlaneControl.class).setPhysicsLocation(new Vector3f(450, 20, 1600));
		getRootNode().attachChild(unit);
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
					if (vehicleControl != null) {
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

	public PlayerItem setActiveUnit(PlayerUnit unit, int id) {
		if (id < 0 || id > 3) {
			logger.error("id invalid: {}", id);
			return activeUnit;
		}
		switch (unit) {
			case CARRIER:
				activeUnit = carrier;
				break;
			case MANTA:
				activeUnit = manta.get(id);
				break;
			case WALRUS:
				activeUnit = walrus.get(id);
				break;
		}
		activeUnit.setCameraToFront();
		return activeUnit;
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
		Projectile projectile = new Projectile("projectile" + System.currentTimeMillis(), assetManager, physicsState.getPhysicsSpace(), rotation);
		ProjectileControl control = projectile.getControl(ProjectileControl.class);
		control.setPhysicsLocation(getActiveUnitControl().getPhysicsLocation().add(rotation.mult(Vector3f.UNIT_Z).mult(15)));
		control.setPhysicsRotation(rotation);
		getRootNode().attachChild(projectile);
	}

	private void fireMissile() {
		GameItem target = findTarget(camNode.getWorldTranslation(), camNode.getWorldRotation(), getRootNode());
		if (target != null) {
			logger.info("fire missile at {}", target.getName());
			Missile missile = new Missile(Constants.MISSILE + System.currentTimeMillis(), assetManager, physicsState.getPhysicsSpace(), getApplication().getRenderManager(), getRootNode(), target);
			MissileControl missileControl = missile.getControl(MissileControl.class);
			Vector3f location = getActiveUnitControl().getPhysicsLocation();
			location.y -= 5; // TODO respect the attitude and angle
			missileControl.setPhysicsLocation(location);
			missileControl.setPhysicsRotation(getActiveUnitControl().getPhysicsRotation().mult(new Quaternion().fromAngles(0, FastMath.PI, 0)));
			getRootNode().attachChild(missile);
		}
	}

	private GameItem findTarget(Vector3f translation, Quaternion rotation, Node rootNode) {
		CollisionResults results = new CollisionResults();
		Ray ray = new Ray(translation, rotation.getRotationColumn(2, null));
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
}
