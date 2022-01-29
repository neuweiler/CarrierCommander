package net.carriercommander.objects.resources;

public class Manta extends ResourceItem {

	@Override
	public String getName() {
		return "Manta Air Defense Fighter";
	}

	@Override
	public String getDescription() {
		return "A long range, versatile aircraft that constitutes the carrier's primary defense against airbourne attack. It also provides essential air cover and support to ground forces when invading an island.";
	}

	@Override
	public String getIconFileName() {
		return "manta.png";
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
