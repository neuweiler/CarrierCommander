package net.carriercommander.ui.hud.carrier;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.CallMethodAction;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.IconComponent;
import net.carriercommander.objects.resources.ResourceItem;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.hud.widgets.ToggleButton;
import net.carriercommander.ui.hud.widgets.ToggleGroup;
import net.carriercommander.ui.hud.widgets.Window;

public class StateResourceEditor extends WindowState {
	private ResourceItem item = null;
	private Label bluePrint;
	private Label name, description;
	private Label amountCarrier, amountStock, amountDrone;
	private Label maxProduction;
	private ToggleButton prioLow, prioMedium, prioHigh;

	@Override
	protected void initialize(Application app) {
		window = new Window(500, 400);

		Container infoPanel = window.addChild(new Container());
		bluePrint = new Label(null);
		infoPanel.addChild(bluePrint, 1);
		Container textPanel = infoPanel.addChild(new Container(), 2);
		name = new Label(null);
		textPanel.addChild(name);
		description = new Label(null);
		description.setMaxWidth(400);
		description.setPreferredSize(new Vector3f(400, 50, 0));
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
	}

	@Override
	protected void cleanup(Application app) {
	}

	private void addItem() {
		if (item.getAmountStock() > 0 && item.getAmountCarrier() < item.getMaxAmountCarrier()) {
			item.setAmountStock(item.getAmountStock() - 1);
			item.setAmountDrone(item.getAmountDrone() + 1);
		}
		updateItem();
	}

	private void removeItem() {
		if (item.getAmountDrone() > 0) {
			item.setAmountStock(item.getAmountStock() + 1);
			item.setAmountDrone(item.getAmountDrone() - 1);
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
		amountCarrier.setText(Integer.toString(item.getAmountCarrier()));
		amountStock.setText(Integer.toString(item.getAmountStock()));
		amountDrone.setText(Integer.toString(item.getAmountDrone()));
		maxProduction.setText(Integer.toString(item.getMaxProduction()));
	}

	public void setResourceItem(ResourceItem item) {
		this.item = item;

		bluePrint.setIcon(new IconComponent("/Interface/hud/carrier/resources/" + item.getIconFileName())); // TODO use blueprint
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
