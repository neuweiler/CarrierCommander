package net.carriercommander;

import com.jme3.app.Application;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;
import net.carriercommander.ui.AbstractState;

public class StateSky extends AbstractState {

	private Spatial sky;

	@Override
	protected void initialize(Application app) {
		sky = SkyFactory.createSky(app.getAssetManager(), "Scenes/FullskiesSunset0068.dds", SkyFactory.EnvMapType.CubeMap);
		sky.setLocalScale(350);
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
		getRootNode().attachChild(sky);
	}

	@Override
	protected void onDisable() {
		sky.removeFromParent();
	}
}
