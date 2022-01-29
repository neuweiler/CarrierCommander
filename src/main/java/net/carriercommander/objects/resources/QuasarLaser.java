package net.carriercommander.objects.resources;

public class QuasarLaser extends ResourceItem {

	@Override
	public String getName() {
		return "Quasar Ground Attack Laser";
	}

	@Override
	public String getDescription() {
		return "Medium power multi-beam laser for use against ground based targets. The unit is powered by the aircraft's engines, giving an unlimited number of rounds.";
	}

	@Override
	public String getIconFileName() {
		return "quasarLaser.png";
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
