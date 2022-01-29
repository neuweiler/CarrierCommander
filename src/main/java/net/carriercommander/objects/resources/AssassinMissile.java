package net.carriercommander.objects.resources;

public class AssassinMissile extends ResourceItem {

	@Override
	public String getName() {
		return "Assassin Air-to-air Missile";
	}

	@Override
	public String getDescription() {
		return "A short range heat seeking missile. It's main use is as an anti-aircraft missile in aerial combat.";
	}

	@Override
	public String getIconFileName() {
		return "assassinMissile.png";
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
