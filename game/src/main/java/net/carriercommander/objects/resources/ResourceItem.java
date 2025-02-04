package net.carriercommander.objects.resources;

public abstract class ResourceItem {
	public enum PRIORITY {LOW, MEDIUM, HIGH}

	private PRIORITY priority = PRIORITY.LOW;
	private int maxProduction = 0;

	private final String name;
	private final String description;
	private final String iconFilename;
	private final String bluePrintFilename;
	private final int weight;

	protected ResourceItem(String name, String description, String iconFilename, String bluePrintFilename, int weight) {
		this.name = name;
		this.description = description;
		this.iconFilename = iconFilename;
		this.bluePrintFilename = bluePrintFilename;
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getIconFileName() {
		return iconFilename;
	}

	public String getBluePrintFileName() {
		return bluePrintFilename;
	}

	public int getWeight() {
		return weight;
	}

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
}
