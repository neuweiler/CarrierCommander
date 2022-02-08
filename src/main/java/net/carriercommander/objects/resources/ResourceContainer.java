package net.carriercommander.objects.resources;

public class ResourceContainer {
	private static final int DEFAULT_WEIGHT_LIMIT = 10000;

	private ResourceItem item;
	private int amount;
	private int weightLimit;

	public ResourceContainer(ResourceItem item, int amount) {
		this(item, amount, DEFAULT_WEIGHT_LIMIT);
	}

	public ResourceContainer(ResourceItem item, int amount, int weightLimit) {
		this.item = item;
		this.amount = amount;
		this.weightLimit = weightLimit;
	}

	public ResourceItem getItem() {
		return item;
	}

	public int getAmount() {
		return amount;
	}

	public boolean add(int number) {
		amount += number;
		if (amount < 0) {
			amount = 0;
			return false;
		}
		if (item.getWeight() * amount > weightLimit) {
			//TODO inform user and play bing
			amount = Math.floorDiv(weightLimit, item.getWeight());
			return false;
		}
		return true;
	}
}
