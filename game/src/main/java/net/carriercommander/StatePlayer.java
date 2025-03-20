package net.carriercommander;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.VehicleControl;
import com.jme3.collision.CollisionResult;
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
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import net.carriercommander.control.MissileControl;
import net.carriercommander.control.PlayerControl;
import net.carriercommander.control.ProjectileControl;
import net.carriercommander.objects.Manta;
import net.carriercommander.objects.Walrus;
import net.carriercommander.objects.*;
import net.carriercommander.objects.resources.*;
import net.carriercommander.ui.AbstractState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class StatePlayer extends AbstractState {
	Logger logger = LoggerFactory.getLogger(StatePlayer.class);

	private final CameraNode camNode;
	private InputManager inputManager;

	private boolean mouseGrabbed = false;
	private PlayerItem activeUnit;
	private Player player;
	private final Vector3f startPosition;

	public StatePlayer(CameraNode camNode, Vector3f startPosition) {
		this.camNode = camNode;
		this.startPosition = startPosition;
	}

	@Override
	protected void initialize(Application app) {
		inputManager = app.getInputManager();
		player = new Player((SimpleApplication) app, -1);

		initPlayer();
		initInput();
	}

	@Override
	protected void cleanup(Application app) {

	}

	@Override
	protected void onEnable() {
	}

	@Override
	protected void onDisable() {
	}

	private void initPlayer() {
		createCarrier();
		createWalrus();
		createManta();
		createSupplyDrone();
	}

	public Player getPlayer() {
		return player;
	}

	private void createCarrier() {
		Carrier carrier = new Carrier(this, Constants.CARRIER, camNode);
		carrier.getControl().setPhysicsLocation(startPosition);
		player.addItem(carrier);
		addCarrierResources(carrier);
	}

	private void addCarrierResources(Carrier carrier) {
		carrier.getResourceManager().addContainer(Arrays.asList(
				new ResourceContainer(new AssassinMissile(), 32),
				new ResourceContainer(new HarbingerMissile(), 24),
				new ResourceContainer(new HammerHeadMissile(), 4),
				new ResourceContainer(new FragmenationBomb(), 8),
				new ResourceContainer(new QuasarLaser(), 4),
				new ResourceContainer(new PulseLaser(), 4),
				new ResourceContainer(new DefenseDrone(), 12),
				new ResourceContainer(new CommPod(), 2),
				new ResourceContainer(new VirusBomb(), 8),
				new ResourceContainer(new CommandCenter(), 4),
				new ResourceContainer(new DecoyFlare(), 10),
				new ResourceContainer(new ReconDrone(), 4),
				new ResourceContainer(new FuelPod(), 4),
				new ResourceContainer(new net.carriercommander.objects.resources.Manta(), 4),
				new ResourceContainer(new net.carriercommander.objects.resources.Walrus(), 4)
		));
	}

	private void createWalrus() {
		Walrus walrus = new Walrus(this, Constants.WALRUS + "_1", camNode);
		walrus.getControl().setPhysicsLocation(startPosition.add(300, 0, 0));
		player.addItem(walrus);

		walrus = new Walrus(this, Constants.WALRUS + "_2", camNode);
		walrus.getControl().setPhysicsLocation(startPosition.add(250, 0, 0));
		player.addItem(walrus);

		walrus = new Walrus(this, Constants.WALRUS + "_3", camNode);
		walrus.getControl().setPhysicsLocation(startPosition.add(200, 0, 0));
		player.addItem(walrus);

		walrus = new Walrus(this, Constants.WALRUS + "_4", camNode);
		walrus.getControl().setPhysicsLocation(startPosition.add(150, 0, 0));
		player.addItem(walrus);
	}

	private void createManta() {
		Manta manta = new Manta(this, Constants.MANTA + "_1", camNode);
		manta.getControl().setPhysicsLocation(startPosition.add(300, 20, -100));
		player.addItem(manta);

		manta = new Manta(this, Constants.MANTA + "_2", camNode);
		manta.getControl().setPhysicsLocation(startPosition.add(250, 20, -100));
		player.addItem(manta);

		manta = new Manta(this, Constants.MANTA + "_3", camNode);
		manta.getControl().setPhysicsLocation(startPosition.add(200, 20, -100));
		player.addItem(manta);

		manta = new Manta(this, Constants.MANTA + "_4", camNode);
		manta.getControl().setPhysicsLocation(startPosition.add(150, 20, -100));
		player.addItem(manta);
	}

	private void createSupplyDrone() {
		SupplyDrone supplyDrone = new SupplyDrone(this, Constants.SUPPLY_DRONE);
		supplyDrone.getControl().setPhysicsLocation(startPosition.add(150, 20, -150));
		player.addItem(supplyDrone);
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
					VehicleControl vehicleControl = (activeUnit != null ? activeUnit.getControl(VehicleControl.class) : null);
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

	public PlayerItem setActiveUnit(PlayerItem.Type type, int id) {
		if (id < 1 || id > 4) {
			logger.error("id invalid: {}", id);
			return activeUnit;
		}
		switch (type) {
			case CARRIER:
				activeUnit = (PlayerItem) player.getItem(Constants.CARRIER);
				break;
			case MANTA:
				activeUnit = (PlayerItem) player.getItem(Constants.MANTA + "_" + id);
				break;
			case WALRUS:
				activeUnit = (PlayerItem) player.getItem(Constants.WALRUS + "_" + id);
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

	private Vector3f canonOffsetManta = new Vector3f(0, 1.1f, 7f);
	private Vector3f canonOffsetWalrus = new Vector3f(0, 1.1f, 7f);

	private void fireCanon() {
		Quaternion rotation = calculateOffsetRotation(getActiveUnitControl(), 0f);
		Vector3f position = calculateOffsetPosition(getActiveUnitControl(), canonOffsetManta);

		Projectile projectile = new Projectile(this, "projectile" + System.currentTimeMillis(), rotation);
		ProjectileControl control = projectile.getControl(ProjectileControl.class);
		control.setPhysicsLocation(position);
		control.setPhysicsRotation(rotation);

		player.addItem(projectile);
	}

	private Vector3f missileOffsetManta = new Vector3f(0, -1.3f, 7);
	private Vector3f missileOffsetWalrus = new Vector3f(0, -1.3f, 7);

	private void fireMissile() {
		GameItem target = findTarget(camNode);
		if (target != null) {
			logger.info("fire missile at {}", target.getName());

			Quaternion rotation = calculateOffsetRotation(getActiveUnitControl(), 180f);
			Vector3f position = calculateOffsetPosition(getActiveUnitControl(), missileOffsetManta);

			Missile missile = new Missile(this, Constants.MISSILE + System.currentTimeMillis(), target);
			MissileControl control = missile.getControl(MissileControl.class);
			control.setPhysicsLocation(position);
			control.setPhysicsRotation(rotation);

			player.addItem(missile);
		}
	}

	/**
	 * Try to find if there is a GameItem under the cross-hairs.
	 *
	 * @param node giving the position and rotation from where we're aiming (usually the current camera node)
	 * @return the GameItem currently in the cross-hairs or null
	 */
	private GameItem findTarget(Node node) {
		CollisionResults results = new CollisionResults();

		Ray ray = new Ray(node.getWorldTranslation(), camNode.getWorldRotation().getRotationColumn(2, null));
		getRootNode().collideWith(ray, results);

		CollisionResult closestCollision = results.getClosestCollision();
		if (closestCollision != null) {
			Geometry geometry = closestCollision.getGeometry();
			if (geometry != null) {
				Node parent = geometry.getParent();
				while (parent != null) {
					if (parent instanceof GameItem) {
						return (GameItem) parent;
					}
					parent = parent.getParent();
				}
			}
		}
		return null;
	}

	/**
	 * Calculate the offset position for an object relative to the given control's position and rotation in the world.
	 *
	 * @param control used to get the start position and rotation
	 * @param offset  to add to the start position in relation to the control's current rotation
	 *                (axis: x=left/right, y=up/down, z=forward/backward)
	 * @return the translated position
	 */
	private Vector3f calculateOffsetPosition(PlayerControl control, Vector3f offset) {
		Vector3f playerPosition = control.getPhysicsLocation();
		Quaternion playerRotation = control.getPhysicsRotation();

		// Rotate the offset vector according to the player's rotation
		Vector3f rotatedOffset = playerRotation.mult(offset);

		// Combine the distances directly into one calculation
		return playerPosition.add(rotatedOffset);
	}

	/**
	 * Calculate the offset angle for an object relative to the given control's rotation.
	 *
	 * @param control    use to get the start rotation
	 * @param anglePitch the angle offset for the pitch
	 * @return the translated rotation
	 */
	private Quaternion calculateOffsetRotation(PlayerControl control, float anglePitch) {
		Quaternion rotation = control.getPhysicsRotation();
		if (anglePitch != 0) {
			Quaternion correction = new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * anglePitch, Vector3f.UNIT_X);
			rotation.multLocal(correction);
		}
		return rotation;
	}
}
