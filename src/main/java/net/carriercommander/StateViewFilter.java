package net.carriercommander;

import com.jme3.app.Application;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.filters.LightScatteringFilter;
import com.jme3.post.filters.TranslucentBucketFilter;
import com.jme3.water.WaterFilter;
import net.carriercommander.ui.AbstractState;

public class StateViewFilter extends AbstractState {
	private FilterPostProcessor fpp;

	@Override
	protected void initialize(Application app) {
		fpp = new FilterPostProcessor(app.getAssetManager());

		WaterFilter water = getState(StateWater.class).getWater();
		if (water != null) {
			fpp.addFilter(water);
		}

		BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
		bloom.setExposurePower(35);
		bloom.setBloomIntensity(.5f);
		fpp.addFilter(bloom);

		LightScatteringFilter lsf = new LightScatteringFilter(Constants.LIGHT_DIRECTION.mult(-300));
		lsf.setLightDensity(1.0f);
		fpp.addFilter(lsf);

/*		DepthOfFieldFilter dof = new DepthOfFieldFilter();
		dof.setFocusDistance(1000);
		dof.setFocusRange(1000);
		fpp.addFilter(dof);
*/
		fpp.addFilter(new TranslucentBucketFilter());
		fpp.setNumSamples(4);
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
}
