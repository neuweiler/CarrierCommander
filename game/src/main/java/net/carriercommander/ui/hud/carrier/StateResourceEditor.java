package net.carriercommander.ui.hud.carrier;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.CallMethodAction;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.IconComponent;
import net.carriercommander.Constants;
import net.carriercommander.Player;
import net.carriercommander.objects.Carrier;
import net.carriercommander.objects.SupplyDrone;
import net.carriercommander.objects.resources.ResourceContainer;
import net.carriercommander.objects.resources.ResourceItem;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.hud.widgets.ToggleButton;
import net.carriercommander.ui.hud.widgets.ToggleGroup;
import net.carriercommander.ui.hud.widgets.Window;

public class StateResourceEditor extends WindowState {
	private ResourceContainer container;
	private ResourceItem item;
	private Class itemClass;
	private final Player player;
	private final Carrier carrier;
	private final SupplyDrone supplyDrone;

	private Label bluePrint;
	private Label name, description;
	private Label amountCarrier, amountStock, amountDrone;
	private Label maxProduction;
	private ToggleButton prioLow, prioMedium, prioHigh;

	public StateResourceEditor(Player player) {
		this.player = player;
		this.carrier = (Carrier) player.getItem(Constants.CARRIER);
		this.supplyDrone = (SupplyDrone) player.getItem(Constants.SUPPLY_DRONE);
	}

	@Override
	protected void initialize(Application app) {
		window = new Window();

		Container infoPanel = window.addChild(new Container());
		bluePrint = new Label(null);
		infoPanel.addChild(bluePrint, 1);
		Container textPanel = infoPanel.addChild(new Container(), 2);
		name = new Label(null);
		textPanel.addChild(name);
		description = new Label(null);
		description.setMaxWidth(300);
		description.setPreferredSize(new Vector3f(300, 50, 0));
		textPanel.addChild(description);

		Container productionPanel = window.addChild((new Container()));
		productionPanel.addChild(new Label("Production: "), 1);
		maxProduction = new Label(null);
		productionPanel.addChild(maxProduction, 2);
		productionPanel.addChild(new ActionButton(new CallMethodAction("+", this, "increaseMaxProduction")), 3);
		productionPanel.addChild(new ActionButton(new CallMethodAction("-", this, "decreaseMaxProduction")), 4);
		ToggleGroup toggleGroup = productionPanel.addChild(new ToggleGroup(), 5);
		prioLow = new ToggleButton(new CallMethodAction("low", this, "setPrioLow"));
		toggleGroup.addChild(prioLow, 1);
		prioMedium = new ToggleButton(new CallMethodAction("medium", this, "setPrioMedium"));
		toggleGroup.addChild(prioMedium, 2);
		prioHigh = new ToggleButton(new CallMethodAction("high", this, "setPrioHigh"));
		toggleGroup.addChild(prioHigh, 3);

		Container transferPanel = window.addChild((new Container()));
		transferPanel.addChild(new Label("Carrier: "), 1);
		amountCarrier = new Label(null);
		transferPanel.addChild(amountCarrier, 2);
		transferPanel.addChild(new Label("Stock: "), 3);
		amountStock = new Label(null);
		transferPanel.addChild(amountStock, 4);
		transferPanel.addChild(new Label("Drone: "), 5);
		amountDrone = new Label(null);
		transferPanel.addChild(amountDrone, 6);
		transferPanel.addChild(new ActionButton(new CallMethodAction("+", this, "addItem")), 7);
		transferPanel.addChild(new ActionButton(new CallMethodAction("-", this, "removeItem")), 8);

		scaleAndPosition(app.getCamera(), .6f, .65f);
	}

	@Override
	protected void cleanup(Application app) {
	}

	private void addItem() {
		if (player.getResourceManager().add(itemClass, -1)) {
			if (!supplyDrone.getResourceManager().add(itemClass, 1)) {
				player.getResourceManager().add(itemClass, 1);
			}
		}
		updateItem();
	}

	private void removeItem() {
		if (supplyDrone.getResourceManager().add(itemClass, -1)) {
			if (!player.getResourceManager().add(itemClass, 1)) {
				supplyDrone.getResourceManager().add(itemClass, 1);
			}
		}
		updateItem();
	}

	private void increaseMaxProduction() {
		if (item.getMaxProduction() < 100) {
			item.setMaxProduction(item.getMaxProduction() + 1);
		}
		updateItem();
	}

	private void decreaseMaxProduction() {
		if (item.getMaxProduction() > 0) {
			item.setMaxProduction(item.getMaxProduction() - 1);
		}
		updateItem();
	}

	private void setPrioLow() {
		item.setPriority(ResourceItem.PRIORITY.LOW);
	}

	private void setPrioMedium() {
		item.setPriority(ResourceItem.PRIORITY.MEDIUM);
	}

	private void setPrioHigh() {
		item.setPriority(ResourceItem.PRIORITY.HIGH);
	}

	private void updateItem() {
		amountCarrier.setText(Integer.toString(container.getAmount()));
		amountStock.setText(Integer.toString(player.getResourceManager().getAmount(itemClass)));
		amountDrone.setText(Integer.toString(supplyDrone.getResourceManager().getAmount(itemClass)));
		maxProduction.setText(Integer.toString(item.getMaxProduction()));
	}

	public void selectResource(int index) {
		this.container = carrier.getResourceManager().getContainer(index);
		item = container.getItem();
		itemClass = item.getClass();

		bluePrint.setIcon(new IconComponent("/Interface/hud/carrier/resources/" + item.getBluePrintFileName()));
		name.setText(item.getName());
		description.setText(item.getDescription());
		prioLow.setSelected(false);
		prioMedium.setSelected(false);
		prioHigh.setSelected(false);
		switch (item.getPriority()) {
			case LOW:
				prioLow.setSelected(true);
				break;
			case MEDIUM:
				prioMedium.setSelected(true);
				break;
			case HIGH:
				prioHigh.setSelected(true);
				break;
		}
		updateItem();

		this.setEnabled(true);
	}
}
