package net.carriercommander;

import com.jme3.app.Application;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import net.carriercommander.ui.AbstractState;

public class StateLights extends AbstractState {
	private DirectionalLightShadowRenderer shadowRenderer;
	private DirectionalLight sun;
	private AmbientLight ambientLight;

	@Override
	protected void initialize(Application app) {
		shadowRenderer = new DirectionalLightShadowRenderer(app.getAssetManager(), 2048, 3);
		shadowRenderer.setLambda(0.55f);
		shadowRenderer.setShadowIntensity(0.6f);
		shadowRenderer.setEdgeFilteringMode(EdgeFilteringMode.Bilinear);

		sun = new DirectionalLight();
		sun.setDirection(Constants.LIGHT_DIRECTION);
		sun.setColor(ColorRGBA.White.clone().multLocal(1.1f));
		shadowRenderer.setLight(sun);

		ambientLight = new AmbientLight();
		ambientLight.setColor(ColorRGBA.White.mult(.3f));
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
		getApplication().getViewPort().addProcessor(shadowRenderer);
		getRootNode().addLight(sun);
		getRootNode().addLight(ambientLight);
	}

	@Override
	protected void onDisable() {
		getRootNode().removeLight(ambientLight);
		getRootNode().removeLight(sun);
		getApplication().getViewPort().removeProcessor(shadowRenderer);
	}
}
