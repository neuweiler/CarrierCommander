package net.carriercommander;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;
import net.carriercommander.control.BaseControl;
import net.carriercommander.network.model.PlayerData;
import net.carriercommander.objects.Carrier;
import net.carriercommander.objects.GameItem;
import net.carriercommander.objects.Manta;
import net.carriercommander.objects.Walrus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Container to store the GameItem and Controls of remote player items
 */
public class Player {
	private final int id;
	private GameItem carrier;
	private final Map<Integer, GameItem> walrus = new HashMap<>(PlayerData.NUM_WALRUS);
	private final Map<Integer, GameItem> manta = new HashMap<>(PlayerData.NUM_MANTA);

	public Player(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public GameItem getCarrier() {
		return carrier;
	}

	public void setCarrier(GameItem carrier) {
		this.carrier = carrier;
	}

	public Map<Integer, GameItem> getWalrus() {
		return walrus;
	}

	public Map<Integer, GameItem> getManta() {
		return manta;
	}

}
