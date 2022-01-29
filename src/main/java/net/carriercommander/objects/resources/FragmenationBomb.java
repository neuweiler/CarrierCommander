package net.carriercommander.objects.resources;

public class FragmenationBomb extends ResourceItem {

	@Override
	public String getName() {
		return "Quaker Fragmentation Bomb";
	}

	@Override
	public String getDescription() {
		return "A 500kg multi-impact bomb which can strike the ground several times before finally detonating near its target. Can be used against floating targets but does not bounce on water.";
	}

	@Override
	public String getIconFileName() {
		return "fragmentationBomb.png";
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
		return 500;
	}
}
