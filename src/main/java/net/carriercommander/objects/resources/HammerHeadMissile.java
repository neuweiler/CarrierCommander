package net.carriercommander.objects.resources;

public class HammerHeadMissile extends ResourceItem {

	@Override
	public String getName() {
		return "Hammer Head Surface-to-surface Missile";
	}

	@Override
	public String getDescription() {
		return "A long range, low flying cruise missile designed for sea-to-shore and sea-to-sea bomberdment. It is guided by its own internal radar and has a range of three kilometres.";
	}

	@Override
	public String getIconFileName() {
		return "hammerHeadMissile.png";
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
