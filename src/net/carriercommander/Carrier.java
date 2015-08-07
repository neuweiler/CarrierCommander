package net.carriercommander;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;

public class Carrier extends Node {

	private Node camHookCarrierBridge = null;
	private Node camHookCarrierRear = null;
	private Node camHookCarrierFlightDeck = null;
	private Node camHookCarrierLaser = null;
	private Node camHookCarrierFlareLauncher = null;
	private Node camHookCarrierSurfaceMissile = null;

	public Carrier(AssetManager assetManager, BulletAppState bulletAppState, float initialWaterHeight) {
		super();
		attachChild(assetManager.loadModel("Models/AdmiralKuznetsovClasscarrier/carrier.obj"));

		camHookCarrierBridge = new Node();
		attachChild(camHookCarrierBridge);
		camHookCarrierBridge.setLocalTranslation(0, 20, 0);
		camHookCarrierBridge.rotate(0, FastMath.DEG_TO_RAD * 180, 0);

		camHookCarrierRear = new Node();
		attachChild(camHookCarrierRear);
		camHookCarrierRear.setLocalTranslation(-40, 10, 170);
		camHookCarrierRear.rotate(0, FastMath.DEG_TO_RAD * 130, 0);

		camHookCarrierFlightDeck = new Node();
		attachChild(camHookCarrierFlightDeck);
		camHookCarrierFlightDeck.setLocalTranslation(0, 15, 0);
		camHookCarrierFlightDeck.rotate(0, FastMath.DEG_TO_RAD * 180, 0);

		camHookCarrierLaser = new Node();
		attachChild(camHookCarrierLaser);
		camHookCarrierLaser.setLocalTranslation(0, 20, 20);
		camHookCarrierLaser.rotate(0, FastMath.DEG_TO_RAD * 180, 0);

		camHookCarrierFlareLauncher = new Node();
		attachChild(camHookCarrierFlareLauncher);
		camHookCarrierFlareLauncher.setLocalTranslation(0, 15, 10);
		camHookCarrierFlareLauncher.rotate(0, FastMath.DEG_TO_RAD * 180, 0);

		camHookCarrierSurfaceMissile = new Node();
		attachChild(camHookCarrierSurfaceMissile);
		camHookCarrierSurfaceMissile.setLocalTranslation(0, 15, 10);
		camHookCarrierSurfaceMissile.rotate(0, FastMath.DEG_TO_RAD * 0, 0);

		setLocalTranslation(-600, initialWaterHeight + 3, 300);
		// scale(2);

		RigidBodyControl control = new RigidBodyControl(100000000); // a carrier weighs 100'000 tons
		addControl(control);
		bulletAppState.getPhysicsSpace().add(control);

		control.setGravity(new Vector3f());
		control.setAngularVelocity(new Vector3f(0, 0.1f, 0));
		control.setLinearVelocity(getLocalRotation().getRotationColumn(2).mult(-10));

	}
	
	public void setCameraToBridge(CameraNode camNode) {
		if (camNode.getParent() != null)
			camNode.getParent().detachChild(camNode);
		camHookCarrierBridge.attachChild(camNode);
	}
	public void setCameraToRear(CameraNode camNode) {
		if (camNode.getParent() != null)
			camNode.getParent().detachChild(camNode);
		camHookCarrierRear.attachChild(camNode);
	}
	public void setCameraToFlightDeck(CameraNode camNode) {
		if (camNode.getParent() != null)
			camNode.getParent().detachChild(camNode);
		camHookCarrierFlightDeck.attachChild(camNode);
	}
	public void setCameraToLaser(CameraNode camNode) {
		if (camNode.getParent() != null)
			camNode.getParent().detachChild(camNode);
		camHookCarrierLaser.attachChild(camNode);
	}
	public void setCameraToFlareLauncher(CameraNode camNode) {
		if (camNode.getParent() != null)
			camNode.getParent().detachChild(camNode);
		camHookCarrierFlareLauncher.attachChild(camNode);
	}
	public void setCameraToSurfaceMissile(CameraNode camNode) {
		if (camNode.getParent() != null)
			camNode.getParent().detachChild(camNode);
		camHookCarrierSurfaceMissile.attachChild(camNode);
	}
}
