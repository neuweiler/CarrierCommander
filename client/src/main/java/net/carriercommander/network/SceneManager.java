package net.carriercommander.network;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import net.carriercommander.control.ShipControl;
import net.carriercommander.shared.model.PlayerData;

public class SceneManager {

	private Map<Integer, PlayerData> players = new HashMap<Integer, PlayerData>();
	private Map<Integer, Spatial> entities = new HashMap<Integer, Spatial>();
	private SimpleApplication app;
	private int myId;

	public SceneManager(SimpleApplication app) {
		this.myId = -1;
		this.app = app;
	}

	public void addOrRefreshPlayer(int id, PlayerData data) {
		players.put(id, data);
	}

	public void removePlayer(int id) {
		if (players.containsKey(id)) {
			players.remove(id);
		}
		if (entities.containsKey(id)) {
			Spatial player = entities.get(id);
			app.enqueue(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					app.getRootNode().detachChild(player);
					BulletAppState bulletAppState = app.getStateManager().getState(BulletAppState.class);
					bulletAppState.getPhysicsSpace().remove(player);
					return null;
				}
			});

			entities.remove(id);
		}
	}

	public boolean isTherePlayerWithId(int id) {
		return players.containsKey(id);
	}

	public Map<Integer, PlayerData> getPlayerList() {
		return players;
	}

	public void refreshPlayerList(Map<Integer, PlayerData> players) {
		this.players = players;
		updatePlayerEntities();
	}

	private void updatePlayerEntities() {
		Iterator<Entry<Integer, PlayerData>> it = players.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, PlayerData> e = it.next();
			updatePlayerEntity(e.getKey(), e.getValue());
		}
	}

	private void updatePlayerEntity(int id, PlayerData data) {
		if (myId != id) {
			if (!entities.containsKey(id)) {
System.out.println("adding new player");
				loadAndAddPlayer(id, data);
				return;
			}
			Spatial player = entities.get(id);

			app.enqueue(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					player.getControl(ShipControl.class).setPhysicsLocation(data.getCarrier().getLocation());
					player.getControl(ShipControl.class).setPhysicsRotation(data.getCarrier().getRotation());
					player.getControl(ShipControl.class).setLinearVelocity(data.getCarrier().getVelocity());
					return null;
				}
			});
		}
	}

	private void loadAndAddPlayer(int id, PlayerData data) {
		Spatial player = loadSpatial(data);
		entities.put(id, player);

		
		CompoundCollisionShape comp = new CompoundCollisionShape(); // use a simple compound shape to improve performance drastically!
		comp.addChildShape(new BoxCollisionShape(new Vector3f(20, 13, 149)), new Vector3f(0f, -1f, -25f));
		comp.addChildShape(new BoxCollisionShape(new Vector3f(7, 19, 30)), new Vector3f(30f, 30f, 5f));
		ShipControl shipControl = new ShipControl(comp, 100000000);
		player.addControl(shipControl);

//		BetterCharacterControl playerControl = new BetterCharacterControl(1.5f, 6f, 1f);
//		player.addControl(playerControl);
//		playerControl.setJumpForce(new Vector3f(0, 2, 0));
//		playerControl.warp(data.getLocation());

		app.enqueue(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				// app.getRootNode().attachChild(player);
				BulletAppState bulletAppState = app.getStateManager().getState(BulletAppState.class);
				app.getRootNode().attachChild(player);
				bulletAppState.getPhysicsSpace().addAll(player);
				return null;
			}
		});
	}

	private Spatial loadSpatial(PlayerData data) {
		Spatial spatial = app.getAssetManager().loadModel("Models/AdmiralKuznetsovClasscarrier/carrier.obj");
		// spatial.getControl(BetterCharacterControl.class).setViewDirection(data.getViewDirection());
		return spatial;
	}

	public void setMyId(int myId) {
		this.myId = myId;
	}
}
