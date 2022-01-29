package net.carriercommander.objects.resources;

public abstract class ResourceItem {
	public enum PRIORITY {LOW, MEDIUM, HIGH}

	private PRIORITY priority = PRIORITY.LOW;
	private int maxProduction = 0;
	private int amountStock = 10; //TODO set to 0
	private int amountCarrier = 5; //TODO set to 0
	private int amountDrone = 0;

	public abstract String getName();
	public abstract String getDescription();
	public abstract String getIconFileName();
	public abstract String getBluePrintFileName();
	public abstract int getMaxAmountCarrier();
	public abstract int getWeight();

	public PRIORITY getPriority() {
		return priority;
	}

	public void setPriority(PRIORITY priority) {
		this.priority = priority;
	}

	public int getMaxProduction() {
		return maxProduction;
	}

	public void setMaxProduction(int maxProduction) {
		this.maxProduction = maxProduction;
	}

	public int getAmountStock() {
		return amountStock;
	}

	public void setAmountStock(int amountStock) {
		this.amountStock = amountStock;
	}

	public int getAmountCarrier() {
		return amountCarrier;
	}

	public void setAmountCarrier(int amountCarrier) {
		this.amountCarrier = amountCarrier;
	}

	public int getAmountDrone() {
		return amountDrone;
	}

	public void setAmountDrone(int amountDrone) {
		this.amountDrone = amountDrone;
	}
}
