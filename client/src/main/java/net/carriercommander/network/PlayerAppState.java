package net.carriercommander.network;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

import net.carriercommander.shared.model.GameObject;
import net.carriercommander.shared.model.PlayerData;

public class PlayerAppState extends AbstractAppState implements ActionListener {

	private PlayerData playerData;
	private BulletAppState bulletAppState;
	private Node rootNode;
	private InputManager inputManager;
	private AssetManager assetManager;
	
	private boolean forward, backward, left, right;
	private Node player;
	private BetterCharacterControl playerControl;
	private Vector3f walkDirection = new Vector3f();
	private Node camHookFront;
	private CameraNode camNode;
	private Geometry mark;
	
	public PlayerAppState(BulletAppState bulletAppState, Node rootNode, PlayerData playerData, CameraNode camNode) {
		this.bulletAppState = bulletAppState;
		this.playerData = playerData;
		this.rootNode = rootNode;
		this.camNode = camNode;
	}
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		this.inputManager = app.getInputManager();
		this.assetManager = app.getAssetManager();

		initPlayer();
		initInput();
		initMark();
	}
	
	private void initPlayer() {
		player = new Node();
		
		Spatial playerModel = assetManager.loadModel("Models/HoverTank/tankFinalExport.blend");
		player.attachChild(playerModel);
		
		playerControl = new BetterCharacterControl(1.5f, 6f, 1f);
		player.addControl(playerControl);
		playerControl.setJumpForce(new Vector3f(0, 2, 0));
		playerControl.warp(new Vector3f(0, 2, 0));

		bulletAppState.getPhysicsSpace().addAll(player);
		rootNode.attachChild(player);
		
		camHookFront = new Node();
		player.attachChild(camHookFront);
		camHookFront.setLocalTranslation(0, 3, -5);
		camHookFront.rotate(0, FastMath.DEG_TO_RAD * 180, 0);
		if (camNode.getParent() != null)
			camNode.getParent().detachChild(camNode);
		player.attachChild(camNode);

		
		playerData.getCarrier().setLocation(player.getLocalTranslation());
		playerData.getCarrier().setRotation(player.getLocalRotation());
		playerData.getCarrier().setVelocity(walkDirection);
	}
	
	private void initInput() {
		inputManager.addMapping("FWD", new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping("BKW", new KeyTrigger(KeyInput.KEY_DOWN));
		inputManager.addMapping("LEFT", new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping("RIGHT", new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping("SHOOT", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(this,  new String[] {"FWD", "BKW", "LEFT", "RIGHT", "SHOOT"});
	}
	
	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals("FWD")) {
			forward = isPressed;
		} else if (name.equals("BKW")) {
			backward = isPressed;
		} else if (name.equals("LEFT")) {
			left = isPressed;
		} else if (name.equals("RIGHT")) {
			right = isPressed;
		} else if (name.equals("SHOOT") && !isPressed) {
			System.out.println("shooting not implemented yet");
		}
	}
	
	private Quaternion quatLeft = new Quaternion(0, 0.001f, 0, 0.999f);
	private Quaternion quatRight = new Quaternion(0, 0.001f, 0, -0.999f);
	
	@Override
	public void update(float tpf) {
		walkDirection.set(Vector3f.ZERO);
		if (forward) {
			walkDirection.set(playerControl.getViewDirection());
		}
		if (backward) {
			walkDirection.set(playerControl.getViewDirection().negate());
		}
		if (left) {
			Vector3f viewDirection = playerControl.getViewDirection();
			playerControl.setViewDirection(quatLeft.mult(viewDirection).normalize());
		}
		if (right) {
			Vector3f viewDirection = playerControl.getViewDirection();
			playerControl.setViewDirection(quatRight.mult(viewDirection).normalize());
		}
	//	walkDirection.setY(0);
		playerControl.setWalkDirection(walkDirection.multLocal(10));

		// update the playerData
		if(playerData != null) {
			GameObject carrier = playerData.getCarrier();
			if (!carrier.getLocation().equals(player.getLocalTranslation()))
				carrier.setLocation(player.getLocalTranslation());
			if (!carrier.getRotation().equals(player.getLocalRotation()))
				carrier.setRotation(player.getLocalRotation());
			if (!carrier.getVelocity().equals(walkDirection))
				carrier.setVelocity(walkDirection);
		}
	}

	public BetterCharacterControl getPlayerControl() {
		return playerControl;
	}

	protected void initMark() {
		Sphere sphere = new Sphere(30, 30, 0.2f);
		mark = new Geometry("BOOM!", sphere);
		Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mark_mat.setColor("Color", ColorRGBA.Red);
		mark.setMaterial(mark_mat);
	}
}
