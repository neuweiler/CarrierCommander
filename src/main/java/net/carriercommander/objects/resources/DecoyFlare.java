package net.carriercommander.objects.resources;

public class DecoyFlare extends ResourceItem {

	@Override
	public String getName() {
		return "Decoy Flare";
	}

	@Override
	public String getDescription() {
		return "High output infra-red generators which are launched from the carrier to confuse incoming heat seeking missiles.";
	}

	@Override
	public String getIconFileName() {
		return "decoyFlare.png";
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
