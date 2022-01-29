package net.carriercommander.objects.resources;

public class Walrus extends ResourceItem {

	@Override
	public String getName() {
		return "Walrus Amphibious Assault Vehicle";
	}

	@Override
	public String getDescription() {
		return "A heavily armed and armoured assault craft that provides the carrier with its invasion capability. This vehicle's main weakness is its inability to copy with an airborne enemy.";
	}

	@Override
	public String getIconFileName() {
		return "walrus.png";
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
