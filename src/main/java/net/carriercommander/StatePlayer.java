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
import net.carriercommander.control.MissileControl;
import net.carriercommander.control.PlayerControl;
import net.carriercommander.control.ProjectileControl;
import net.carriercommander.objects.*;
import net.carriercommander.ui.AbstractState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatePlayer extends AbstractState {
	Logger logger = LoggerFactory.getLogger(StatePlayer.class);

	public enum PlayerUnit { CARRIER, WALRUS, MANTA }

	private final CameraNode camNode;
	private WaterFilter water;
	private InputManager inputManager;
	private AssetManager assetManager;
	private BulletAppState physicsState;

	private boolean mouseGrabbed = false;
	private PlayerItem activeUnit;
	private Player player = new Player(-1);

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
		activeUnit = (PlayerItem) getRootNode().getChild(Constants.CARRIER);
		activeUnit.setCameraToFront();
	}

	@Override
	protected void onDisable() {
	}

	private void initPlayer() {
		createCarrier();
		createWalrus();
		createManta();
	}

	public Player getPlayer() {
		return player;
	}

	private void createCarrier() {
		Carrier carrier = new Carrier(Constants.CARRIER, assetManager, physicsState, water, camNode);
		carrier.getControl().setPhysicsLocation(new Vector3f(300, (water != null ? water.getWaterHeight() : 0), 1700));
		player.setCarrier(carrier);
		getRootNode().attachChild(carrier);
	}

	private void createWalrus() {
		Walrus walrus = new Walrus(Constants.WALRUS + "_1", assetManager, physicsState, water, camNode);
		walrus.getControl().setPhysicsLocation(new Vector3f(850, 5, 1700));
		player.getWalrus().put(0, walrus);
		getRootNode().attachChild(walrus);

		walrus = new Walrus(Constants.WALRUS + "_2", assetManager, physicsState, water, camNode);
		walrus.getControl().setPhysicsLocation(new Vector3f(550, 0, 1700));
		player.getWalrus().put(1, walrus);
		getRootNode().attachChild(walrus);

		walrus = new Walrus(Constants.WALRUS + "_3", assetManager, physicsState, water, camNode);
		walrus.getControl().setPhysicsLocation(new Vector3f(500, 0, 1700));
		player.getWalrus().put(2, walrus);
		getRootNode().attachChild(walrus);

		walrus = new Walrus(Constants.WALRUS + "_4", assetManager, physicsState, water, camNode);
		walrus.getControl().setPhysicsLocation(new Vector3f(450, 0, 1700));
		player.getWalrus().put(3, walrus);
		getRootNode().attachChild(walrus);
	}

	private void createManta() {
		Manta unit = new Manta(Constants.MANTA + "_1", assetManager, physicsState, camNode);
		unit.getControl().setPhysicsLocation(new Vector3f(850, (water != null ? water.getWaterHeight() : 0) + 15, 1700));
		player.getManta().put(0, unit);
		getRootNode().attachChild(unit);

		unit = new Manta(Constants.MANTA + "_2", assetManager, physicsState, camNode);
		unit.getControl().setPhysicsLocation(new Vector3f(550, 20, 1600));
		player.getManta().put(1, unit);
		getRootNode().attachChild(unit);

		unit = new Manta(Constants.MANTA + "_3", assetManager, physicsState, camNode);
		unit.getControl().setPhysicsLocation(new Vector3f(500, 20, 1600));
		player.getManta().put(2, unit);
		getRootNode().attachChild(unit);

		unit = new Manta(Constants.MANTA + "_4", assetManager, physicsState, camNode);
		unit.getControl().setPhysicsLocation(new Vector3f(450, 20, 1600));
		player.getManta().put(3, unit);
		getRootNode().attachChild(unit);
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
				activeUnit = (PlayerItem) player.getCarrier();
				break;
			case MANTA:
				activeUnit = (PlayerItem) player.getManta().get(id);
				break;
			case WALRUS:
				activeUnit = (PlayerItem) player.getWalrus().get(id);
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
