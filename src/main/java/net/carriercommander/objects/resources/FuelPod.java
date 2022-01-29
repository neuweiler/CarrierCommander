package net.carriercommander.objects.resources;

public class FuelPod extends ResourceItem {

	@Override
	public String getName() {
		return "Fuel Transfer Pod";
	}

	@Override
	public String getDescription() {
		return "An auxiliary fuel tank that allows one AAV to refuel another. The pos is a sealed unit filled at the factory, it is discarded after use.";
	}

	@Override
	public String getIconFileName() {
		return "fuelPod.png";
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
