package net.carriercommander.objects.resources;

public class VirusBomb extends ResourceItem {

	@Override
	public String getName() {
		return "Command Centre Virus Bomb";
	}

	@Override
	public String getDescription() {
		return "Enables islands to be taken from the enemy while leaving most structures on the island intact. Must be fired into the command centre from close range.";
	}

	@Override
	public String getIconFileName() {
		return "virusBomb.png";
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
