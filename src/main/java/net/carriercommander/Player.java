package net.carriercommander;

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

	public Player(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void addItem(GameItem gameItem) {
		items.put(gameItem.getName(), gameItem);
	}

	public void removeItem(GameItem gameItem) {
		items.remove(gameItem);
	}

	public GameItem getItem(String name) {
		return items.get(name);
	}

	public Collection<GameItem> getItems() {
		return items.values();
	}

}
