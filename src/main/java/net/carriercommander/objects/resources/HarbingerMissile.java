package net.carriercommander.objects.resources;

public class HarbingerMissile extends ResourceItem {

	@Override
	public String getName() {
		return "Harbinger Surface-to-surface Missile";
	}

	@Override
	public String getDescription() {
		return "An AAV based short range missile for use against ground targets. It is wire guided, requiring operator control during flight.";
	}

	@Override
	public String getIconFileName() {
		return "harbingerMissile.png";
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
