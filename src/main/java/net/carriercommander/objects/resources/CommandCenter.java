package net.carriercommander.objects.resources;

public class CommandCenter extends ResourceItem {

	@Override
	public String getName() {
		return "Automatic Command Center Builder";
	}

	@Override
	public String getDescription() {
		return "When dropped from an AAV in a suitable place, an iisland command centre is constructed using locally obtained materials.";
	}

	@Override
	public String getIconFileName() {
		return "commandCenter.png";
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
