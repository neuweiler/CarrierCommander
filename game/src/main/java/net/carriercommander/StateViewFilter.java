package net.carriercommander;

import com.jme3.app.Application;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.*;
import com.jme3.water.WaterFilter;
import net.carriercommander.ui.AbstractState;

public class StateViewFilter extends AbstractState {
	private FilterPostProcessor fpp;

	private WaterFilter water;
	private BloomFilter bloom;
	private LightScatteringFilter lsf;
	private TranslucentBucketFilter tbf;
	private FogFilter fog;
	private FXAAFilter fxaa;

	@Override
	protected void initialize(Application app) {
		fpp = new FilterPostProcessor(app.getAssetManager());

		water = getState(StateWater.class).getWater();
		if (water != null) {
			fpp.addFilter(water);
		}

		bloom = new BloomFilter(BloomFilter.GlowMode.SceneAndObjects);
		fpp.addFilter(bloom);

		lsf = new LightScatteringFilter(Constants.LIGHT_DIRECTION.mult(-300));
//		lsf.setLightDensity(1.0f);
		fpp.addFilter(lsf);

		tbf = new TranslucentBucketFilter();
		fpp.addFilter(tbf);

		fog = new FogFilter();
		fpp.addFilter(fog);

		fxaa = new FXAAFilter();
		fpp.addFilter(fxaa);
	}

	@Override
	protected void cleanup(Application app) {
		fpp = null;
	}

	@Override
	protected void onEnable() {
		getApplication().getViewPort().addProcessor(fpp);
	}

	@Override
	protected void onDisable() {
		getApplication().getViewPort().removeProcessor(fpp);
	}

	public void setBloomEnabled(boolean enabled) {
		bloom.setEnabled(enabled);
	}

	public void setWaterEnabled(boolean enabled) {
		water.setEnabled(enabled);
	}

	public void setLightScatteringEnabled(boolean enabled) {
		lsf.setEnabled(enabled);
	}

	public void setTranslucentBucketEnabled(boolean enabled) {
		tbf.setEnabled(enabled);
	}

	public void setFogEnabled(boolean enabled) {
		fog.setEnabled(enabled);
	}

	public void setFXAAEnabled(boolean enabled) {
		fxaa.setEnabled(enabled);
	}

	public void setNumSamples(int samples) {
		fpp.setNumSamples(samples);
	}
}
