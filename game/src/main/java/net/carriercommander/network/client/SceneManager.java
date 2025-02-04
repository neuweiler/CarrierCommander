package net.carriercommander.network.client;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import net.carriercommander.Player;
import net.carriercommander.control.BaseControl;
import net.carriercommander.network.model.config.Island;
import net.carriercommander.network.model.player.GameItemData;
import net.carriercommander.network.model.player.ItemType;
import net.carriercommander.objects.*;
import net.carriercommander.ui.menu.StateLoadGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the scene by adding, updating and removing remote player game items
 */
public class SceneManager {
	private final Logger logger = LoggerFactory.getLogger(SceneManager.class);

	private final Map<Integer, Player> players = new HashMap<>();
	private final SimpleApplication app;
	private int myPlayerId;

	public SceneManager(SimpleApplication app) {
		this.myPlayerId = -1;
		this.app = app;
	}

	public Player addPlayer(int id) {
		if (myPlayerId == id) {
			return null;
		}
		logger.info("adding new player {}", id);
		Player player = new Player(app, id);
		players.put(id, player);

		return player;
	}

	/**
	 * Remove a remote player's objects from the scene
	 *
	 * @param id of the player
	 */
	public void removePlayer(int id) {
		if (myPlayerId == id) {
			return;
		}
		if (players.containsKey(id)) {
			Player player = players.get(id);
			app.enqueue(() -> player.getItems().forEach(player::removeItem));
			players.remove(id);
		}
	}

	public void updatePlayer(int playerId, Collection<GameItemData> data) {
		if (myPlayerId == playerId) {
			return;
		}
		Player player = players.get(playerId);
		if (player == null) {
			logger.info("received data from new remote player {}", playerId);
			player = addPlayer(playerId);
		}

		Player finalPlayer = player;
		app.enqueue(() -> data.forEach(gameItemData -> updatePlayer(finalPlayer, gameItemData)));
	}

	private void updatePlayer(Player player, GameItemData gameItemData) {
		String itemName = player.getId() + "_" + gameItemData.getId();
		GameItem gameItem = player.getItem(itemName);
		if (gameItem == null) {
			gameItem = createItem(itemName, gameItemData.getType());
			if (gameItem == null) {
				return;
			}
			player.addItem(gameItem);
		}
		updateItem(gameItem, gameItemData);
	}

	private GameItem createItem(String itemName, ItemType type) {
		switch (type) {
			case carrier:
				return createGameItem(itemName,
						Carrier.loadModel(app.getAssetManager()),
						createControl(Carrier.createCollisionShape(), Carrier.MASS));
			case manta:
				return createGameItem(itemName,
						Manta.loadModel(app.getAssetManager()),
						createControl(Manta.createCollisionShape(), Manta.MASS));
			case walrus:
				return createGameItem(itemName,
						Walrus.loadModel(app.getAssetManager()),
						createControl(Walrus.createCollisionShape(), Walrus.MASS));
			case missile:
				return createGameItem(itemName,
						Missile.loadModel(app.getAssetManager()),
						createControl(Missile.createCollisionShape(), Missile.MASS));
			case projectile:
				return createGameItem(itemName,
						Projectile.loadModel(app.getAssetManager()),
						createControl(Projectile.createCollisionShape(), Projectile.MASS));
		}
		logger.error("cannot create remote game item {}", type);
		return null;
	}

	private void updateItem(GameItem item, GameItemData gameItemData) {
		PhysicsRigidBody control = item.getControl();
		if (control != null && gameItemData != null) {
			if (gameItemData.isDestroy()) {
				BulletAppState bulletAppState = app.getStateManager().getState(BulletAppState.class);
				app.getRootNode().detachChild(item);
				bulletAppState.getPhysicsSpace().remove(item);
			} else {
				control.setPhysicsLocation(gameItemData.getLocation());
				control.setPhysicsRotation(gameItemData.getRotation());
				control.setLinearVelocity(gameItemData.getVelocity());
			}
		}
	}

	private GameItem createGameItem(String itemName, Spatial spatial, BaseControl control) {
		GameItem item = new GameItem(itemName);
		item.attachChild(spatial);
		item.addControl(control);
		return item;
	}

	private BaseControl createControl(CollisionShape collisionShape, float mass) {
		BaseControl control = new BaseControl(collisionShape, mass);
		control.setKinematicSpatial(true);
		control.setGravity(Vector3f.ZERO);
		return control;
	}

	public void setMyPlayerId(int myPlayerId) {
		this.myPlayerId = myPlayerId;
	}

	public void init(Vector3f startPosition, List<Island> islands, List<List<String>> connections) {
		app.getStateManager().attach(new StateLoadGame(startPosition, islands, connections));
	}
}
