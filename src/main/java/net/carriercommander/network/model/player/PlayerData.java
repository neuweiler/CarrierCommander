package net.carriercommander.network.model.player;

import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import net.carriercommander.objects.*;

import java.util.*;

/**
 * Object holding all the necessary data required to transfer between client and server.
 *
 * @author Michael Neuweiler
 */
public class PlayerData {

	private int id;
	private final Map<String, GameItemData> itemDatas = new HashMap<>(); //TODO check if thread safety is needed here
	private final Vector3f tmpVec = new Vector3f(); // temporary storage to reduce object creation/destruction
	private final Quaternion tmpQuat = new Quaternion(); // temporary storage to reduce object creation/destruction
	private final List<GameItemData> updatedItems = new ArrayList<>();

	public PlayerData() {
	}

	public List<GameItemData> getAllItems() {
		return new ArrayList<>(itemDatas.values());
	}

	public List<GameItemData> getModifiedItems() {
		return updatedItems;
	}

	public void clear() {
		updatedItems.stream().filter(GameItemData::isDestroy)
				.forEach(itemDatas::remove);

		updatedItems.clear();
	}

	public void update(GameItem item) {
		GameItemData itemData = itemDatas.get(item.getName());
		if (itemData == null) {
			itemData = new GameItemData(item.getName(), getItemType(item));
			itemDatas.put(item.getName(), itemData);
		}

		PhysicsRigidBody control = item.getControl();
		boolean add = itemData.setLocation(control.getPhysicsLocation(tmpVec));
		add |= itemData.setRotation(control.getPhysicsRotation(tmpQuat));
		add |= itemData.setVelocity(control.getLinearVelocity(tmpVec));
		if (add) {
			updatedItems.add(itemData);
		}
	}

	private ItemType getItemType(GameItem item) {
		if (item instanceof Carrier) {
			return ItemType.carrier;
		}
		if (item instanceof Walrus) {
			return ItemType.walrus;
		}
		if (item instanceof Manta) {
			return ItemType.manta;
		}
		if (item instanceof Missile) {
			return ItemType.missile;
		}
		if (item instanceof Projectile) {
			return ItemType.projectile;
		}
		return ItemType.unknown;
	}

	public void remove(GameItem item) {
		GameItemData itemData = itemDatas.get(item.getName());
		if (itemData == null) {
			return;
		}
		itemData.setDestroy(true);
		updatedItems.add(itemData);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
