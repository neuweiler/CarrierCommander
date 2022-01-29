package net.carriercommander.objects.resources;

public class DefenseDrone extends ResourceItem {

	@Override
	public String getName() {
		return "Passive Defense Drone";
	}

	@Override
	public String getDescription() {
		return "Inflatable decoy units designed to provide a sacrificial defense against low level missile attack.";
	}

	@Override
	public String getIconFileName() {
		return "defenseDrone.png";
	}

	@Override
	public String getBluePrintFileName() {
		return "";
	}

	@Override
	public int getMaxAmountCarrier() {
		return 50;
	}

	@Override
	public int getWeight() {
		return 5;
	}
}
