package net.carriercommander.objects.resources;

public class FuelPod extends ResourceItem {

	public FuelPod() {
		super(
				"Fuel Transfer Pod",
				"An auxiliary fuel tank that allows one AAV to refuel another. The pos is a sealed unit filled at the factory, it is discarded after use.",
				"fuelPod.png",
				"fuelPod_bp.png",
				5
		);
	}
}
