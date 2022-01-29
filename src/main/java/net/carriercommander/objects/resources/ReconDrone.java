package net.carriercommander.objects.resources;

public class ReconDrone extends ResourceItem {

	@Override
	public String getName() {
		return "Reconnaissance Drone";
	}

	@Override
	public String getDescription() {
		return "Remote camera equipped with retro-thrusters and target reporting system. Targeting information is relayed from these drones to the guidance system of the hammerhead missiles.";
	}

	@Override
	public String getIconFileName() {
		return "reconDrone.png";
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
