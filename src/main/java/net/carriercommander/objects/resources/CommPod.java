package net.carriercommander.objects.resources;

public class CommPod extends ResourceItem {

	@Override
	public String getName() {
		return "Long Range Communication Pod";
	}

	@Override
	public String getDescription() {
		return "An aircraft payload pod which acts as a booster for the telemetry signals from the carrier. Enables aircraft and, to a lesser extent, AAV's to be deployed far from the carrier.";
	}

	@Override
	public String getIconFileName() {
		return "commPod.png";
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
