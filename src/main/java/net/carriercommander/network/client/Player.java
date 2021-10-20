package net.carriercommander.network.client;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;
import net.carriercommander.network.model.PlayerData;

import java.util.ArrayList;
import java.util.List;

public class Player {
	public int id;

	public Node carrier;
	public RigidBodyControl carrierControl;

	public List<Node> walruses = new ArrayList<>(PlayerData.NUM_WALRUS);
	public List<RigidBodyControl> walrusControls = new ArrayList<>(PlayerData.NUM_WALRUS);

	public List<Node> mantas = new ArrayList<>(PlayerData.NUM_MANTA);
	public List<RigidBodyControl> mantaControls = new ArrayList<>(PlayerData.NUM_MANTA);

	public Player(int id) {
		this.id = id;
	}
}
