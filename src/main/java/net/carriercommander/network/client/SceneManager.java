package net.carriercommander.network.client;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector3f;
import net.carriercommander.Constants;
import net.carriercommander.Player;
import net.carriercommander.control.BaseControl;
import net.carriercommander.objects.Carrier;
import net.carriercommander.objects.GameItem;
import net.carriercommander.objects.Manta;
import net.carriercommander.objects.Walrus;
import net.carriercommander.network.model.GameItemData;
import net.carriercommander.network.model.PlayerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the scene by adding, updating and removing remote player game items
 */
public class SceneManager {
	private final Logger logger = LoggerFactory.getLogger(SceneManager.class);

	private final Map<Integer, Player> players = new HashMap<>();
	private final SimpleApplication app;
	private int myId;

	public SceneManager(SimpleApplication app) {
		this.myId = -1;
		this.app = app;
	}

	/**
	 * Remove a remote player's objects from the scene
	 * @param id of the player
	 */
	public void removePlayer(int id) {
		if (players.containsKey(id)) {
			Player player = players.get(id);
			app.enqueue(() -> {
				app.getRootNode().detachChild(player.getCarrier());
				BulletAppState bulletAppState = app.getStateManager().getState(BulletAppState.class);
				bulletAppState.getPhysicsSpace().remove(player.getCarrier());

				//TODO remove walrus and mantas too
			});

			players.remove(id);
		}
	}

	public void updatePlayers(Map<Integer, PlayerData> players) {
		players.forEach(this::updatePlayer);
	}

	private void updatePlayer(int id, PlayerData data) {
		if (myId == id) {
			return;
		}

		final Player player = (players.containsKey(id) ? players.get(id) : addPlayer(id));
		app.enqueue(() -> {
			updateItem(player.getCarrier(), data.getCarrier());
			for (int index = 0; index < PlayerData.NUM_MANTA; index++) {
				updateItem(player.getManta().get(index), data.getManta(index));
			}
			for (int index = 0; index < PlayerData.NUM_WALRUS; index++) {
				updateItem(player.getWalrus().get(index), data.getWalrus(index));
			}
		});
	}

	private void updateItem(GameItem item, GameItemData gameItemData) {
		PhysicsRigidBody control = item.getControl();
		if (control != null && gameItemData != null) {
			control.setPhysicsLocation(gameItemData.getLocation());
			control.setPhysicsRotation(gameItemData.getRotation());
			control.setLinearVelocity(gameItemData.getVelocity());
		}
	}

	private Player addPlayer(int id) {
		logger.info("adding new player {}", id);
		Player player = new Player(id);
		players.put(id, player);

		player.setCarrier(createRemoteCarrier(id));
		for (int i = 0; i < PlayerData.NUM_MANTA; i++) {
			player.getManta().put(i, createRemoteManta(id, i));
		}
		for (int i = 0; i < PlayerData.NUM_WALRUS; i++) {
			player.getWalrus().put(i, createRemoteWalrus(id, i));
		}
		return player;
	}

	private GameItem createRemoteCarrier(int id) {
		GameItem carrier = new GameItem(Constants.CARRIER + "_" + id);
		carrier.attachChild(Carrier.loadModel(app.getAssetManager()));
		BaseControl carrierControl = createControl(Carrier.createCollisionShape(), Carrier.MASS);
		carrier.addControl(carrierControl);

		app.enqueue(() -> {
			app.getRootNode().attachChild(carrier);
			BulletAppState bulletAppState = app.getStateManager().getState(BulletAppState.class);
			bulletAppState.getPhysicsSpace().addAll(carrier);
		});
		return carrier;
	}

	private GameItem createRemoteManta(int playerId, int id) {
		GameItem manta = new GameItem(Constants.MANTA + "_" + playerId + "_" + id);
		manta.attachChild(Manta.loadModel(app.getAssetManager()));
		BaseControl control = createControl(Manta.createCollisionShape(), Manta.MASS);
		manta.addControl(control);

		app.enqueue(() -> {
			app.getRootNode().attachChild(manta);
			BulletAppState bulletAppState = app.getStateManager().getState(BulletAppState.class);
			bulletAppState.getPhysicsSpace().addAll(manta);
		});
		return manta;
	}

	private GameItem createRemoteWalrus(int playerId, int id) {
		GameItem walrus = new GameItem(Constants.WALRUS + "_" + playerId + "_" + id);
		walrus.attachChild(Walrus.loadModel(app.getAssetManager()));
		BaseControl control = createControl(Walrus.createCollisionShape(), Walrus.MASS);
		walrus.addControl(control);

		app.enqueue(() -> {
			app.getRootNode().attachChild(walrus);
			BulletAppState bulletAppState = app.getStateManager().getState(BulletAppState.class);
			bulletAppState.getPhysicsSpace().addAll(walrus);
		});
		return walrus;
	}

	private BaseControl createControl(CollisionShape collisionShape, float mass) {
		BaseControl control = new BaseControl(collisionShape, mass);
		control.setKinematicSpatial(false);
		control.setGravity(Vector3f.ZERO);
		return control;
	}

	public void setMyId(int myId) {
		this.myId = myId;
	}
}
