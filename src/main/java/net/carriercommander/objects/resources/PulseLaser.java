package net.carriercommander.objects.resources;

public class PulseLaser extends ResourceItem {

	@Override
	public String getName() {
		return "Avatar Pulse Laser";
	}

	@Override
	public String getDescription() {
		return "High power pulsed chemical laser deisgned for AAV or fixed mounting. The unit contains its own power and reactant supply. A fully charged unit can supply 40 shots.";
	}

	@Override
	public String getIconFileName() {
		return "pulseLaser.png";
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
