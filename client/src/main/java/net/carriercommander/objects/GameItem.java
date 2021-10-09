package net.carriercommander.objects;

import com.jme3.scene.Node;

public class GameItem extends Node {

	protected float health = 1.0f;

	public GameItem() {
		super();
	}

	public GameItem(String name) {
		super(name);
	}
}
