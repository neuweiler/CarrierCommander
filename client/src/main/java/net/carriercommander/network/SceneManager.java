package net.carriercommander.network;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import net.carriercommander.control.ShipControl;
import net.carriercommander.objects.Carrier;
import net.carriercommander.objects.Manta;
import net.carriercommander.objects.Walrus;
import net.carriercommander.shared.model.GameObject;
import net.carriercommander.shared.model.MantaData;
import net.carriercommander.shared.model.PlayerData;
import net.carriercommander.shared.model.WalrusData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

public class SceneManager {
	private final Logger logger = LoggerFactory.getLogger(SceneManager.class);

	private final Map<Integer, Player> players = new HashMap<>();
	private final SimpleApplication app;
	private int myId;

	public SceneManager(SimpleApplication app) {
		this.myId = -1;
		this.app = app;
	}

	public void removePlayer(int id) {
		if (players.containsKey(id)) {
			Player player = players.get(id);
			app.enqueue(() -> {
				app.getRootNode().detachChild(player.carrier);
				BulletAppState bulletAppState = app.getStateManager().getState(BulletAppState.class);
				bulletAppState.getPhysicsSpace().remove(player.carrier);

				//TODO remove walrus and mantas too
			});

			players.remove(id);
		}
	}

	public void refreshPlayerList(Map<Integer, PlayerData> players) {
		for (Entry<Integer, PlayerData> e : players.entrySet()) {
			updatePlayerEntity(e.getKey(), e.getValue());
		}
	}

	private void updatePlayerEntity(int id, PlayerData data) {
		if (myId != id) {
			if (!players.containsKey(id)) {
				loadAndAddPlayer(id, data);
			}
			Player player = players.get(id);

			app.enqueue(() -> {
				updateUnit(player.carrierControl, data.getCarrier());
				for (int index = 0; index < PlayerData.NUM_MANTA; index++) {
					updateUnit(player.mantaControls.get(index), data.getManta(index));
				}
				for (int index = 0; index < PlayerData.NUM_WALRUS; index++) {
					updateUnit(player.walrusControls.get(index), data.getWalrus(index));
				}
			});
		}
	}

	private void updateUnit(RigidBodyControl control, GameObject gameObject) {
		if (control != null && gameObject != null) {
			control.setPhysicsLocation(gameObject.getLocation());
			control.setPhysicsRotation(gameObject.getRotation());
			control.setLinearVelocity(gameObject.getVelocity());
		}
	}

	private void loadAndAddPlayer(int id, PlayerData data) {
		logger.info("adding new player {}", id);
		Player player = new Player(id);
		players.put(id, player);

		addCarrier(player);
		for (int i = 0; i < PlayerData.NUM_MANTA; i++) {
			addManta(player);
		}
		for (int i = 0; i < PlayerData.NUM_WALRUS; i++) {
			addWalrus(player);
		}
	}

	private void addCarrier(Player player) {
		player.carrier = new Node();
		player.carrier.attachChild(Carrier.loadModel(app.getAssetManager()));
		player.carrierControl = createControl(Carrier.createCollisionShape(), Carrier.MASS);
		player.carrier.addControl(player.carrierControl);

		app.enqueue(() -> {
			// app.getRootNode().attachChild(player);
			BulletAppState bulletAppState = app.getStateManager().getState(BulletAppState.class);
			app.getRootNode().attachChild(player.carrier);
			bulletAppState.getPhysicsSpace().addAll(player.carrier);
		});
	}

	private void addManta(Player player) {
		Node node = new Node();
		node.attachChild(Manta.loadModel(app.getAssetManager()));
		RigidBodyControl control = createControl(Manta.createCollisionShape(), Manta.MASS);
		node.addControl(control);
		player.mantas.add(node);
		player.mantaControls.add(control);

		app.enqueue(() -> {
			BulletAppState bulletAppState = app.getStateManager().getState(BulletAppState.class);
			app.getRootNode().attachChild(node);
			bulletAppState.getPhysicsSpace().addAll(node);
		});
	}

	private void addWalrus(Player player) {
		Node node = new Node();
		node.attachChild(Walrus.loadModel(app.getAssetManager()));
		RigidBodyControl control = createControl(Walrus.createCollisionShape(), Walrus.MASS);
		node.addControl(control);
		player.walruses.add(node);
		player.walrusControls.add(control);

		app.enqueue(() -> {
			BulletAppState bulletAppState = app.getStateManager().getState(BulletAppState.class);
			app.getRootNode().attachChild(node);
			bulletAppState.getPhysicsSpace().addAll(node);
		});
	}

	private RigidBodyControl createControl(CollisionShape collisionShape, float mass) {
		RigidBodyControl control = new RigidBodyControl(collisionShape, mass);
		control.setKinematicSpatial(false);
		control.setGravity(Vector3f.ZERO);
		return control;
	}

	public void setMyId(int myId) {
		this.myId = myId;
	}
}
