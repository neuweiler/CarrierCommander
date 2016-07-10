package net.carriercommander.shared.model;

import java.util.ArrayList;
import java.util.List;

import com.jme3.network.serializing.Serializable;

@Serializable
public class PlayerData {

	private int id;
	private GameObject carrier = new GameObject();
	private List<GameObject> walrus = new ArrayList<GameObject>(4);
	private List<GameObject> manta = new ArrayList<GameObject>(4);

	public PlayerData() {
		for (int i = 0; i < 4; i++) {
			walrus.add(new GameObject());
			manta.add(new GameObject());
		}
	}

	public boolean modified() {
		boolean modified = carrier.modified();
		for (int i = 0; i < 4; i++) {
			if (walrus.get(i).modified() || manta.get(i).modified()) {
				modified = true;
				break;
			}
		}
		return modified;
	}

	public void clean() {
		carrier.clean();
		for (int i = 0; i < 4; i++) {
			walrus.get(i).clean();
			manta.get(i).clean();
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public GameObject getCarrier() {
		return carrier;
	}

	public GameObject getWalrus(int index) {
		return walrus.get(index);
	}

	public GameObject getManta(int index) {
		return manta.get(index);
	}
	
	
}
