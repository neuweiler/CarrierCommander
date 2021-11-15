package net.carriercommander;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node;
import net.carriercommander.control.BaseControl;
import net.carriercommander.objects.GameItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Container to store the GameItem and Controls of remote player items
 */
public class Player {
	private final int id;
	private final Map<String, GameItem> items = new HashMap<>();
	private final SimpleApplication app;

	public Player(SimpleApplication app, int id) {
		this.app = app;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void addItem(GameItem gameItem) {
		addItem(gameItem, app.getRootNode());
	}

	public void addItem(GameItem gameItem, Node parent) {
		items.put(gameItem.getName(), gameItem);

		gameItem.getControl(BaseControl.class).setPlayer(this);

		app.enqueue(() -> {
			parent.attachChild(gameItem);
			BulletAppState bulletAppState = app.getStateManager().getState(BulletAppState.class);
			bulletAppState.getPhysicsSpace().addAll(gameItem);
		});
		//TODO check if network game and if local GameItem, add to GameData (optional)
	}

	public void removeItem(GameItem gameItem) {
		items.remove(gameItem);

		app.enqueue(() -> {
			BulletAppState bulletAppState = app.getStateManager().getState(BulletAppState.class);
			bulletAppState.getPhysicsSpace().removeAll(gameItem);
			gameItem.removeFromParent();
		});

		//TODO check if network game and if local GameItem , then flag GameData for removal
	}

	public GameItem getItem(String name) {
		return items.get(name);
	}

	public Collection<GameItem> getItems() {
		return items.values();
	}

}
