package net.carriercommander;

import com.jme3.app.Application;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.texture.Texture2D;
import com.jme3.water.WaterFilter;
import net.carriercommander.ui.AbstractState;

public class StateWater extends AbstractState {

	private WaterFilter water;
	private float time = 0.0f;
	private final float initialWaterHeight = 0f;

	@Override
	protected void initialize(Application app) {
		water = new WaterFilter();

		water.setWaveScale(0.003f);
		water.setMaxAmplitude(2f);
		water.setFoamExistence(new Vector3f(1f, 4, 0.5f));
		water.setFoamTexture((Texture2D) app.getAssetManager().loadTexture("Common/MatDefs/Water/Textures/foam2.jpg"));
		//water.setUseHQShoreline(true);
		// water.setNormalScale(0.5f);
		// water.setRefractionConstant(0.25f);
		// water.setRefractionStrength(0.2f);
		// water.setFoamHardness(0.6f);
		water.setWaterHeight(initialWaterHeight);
	}

	@Override
	protected void cleanup(Application app) {
		water = null;
	}

	@Override
	protected void onEnable() {
		water.setReflectionScene(getRootNode());
		water.setLightDirection(Constants.LIGHT_DIRECTION);
	}

	@Override
	protected void onDisable() {
		water.setReflectionScene(null);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		updateWaterHeight(tpf);
	}

	private void updateWaterHeight(float tpf) {
		time += tpf;
		if (water != null) {
			float waterHeight = (float) Math.cos(((time * 0.3f) % FastMath.TWO_PI)) * 1.4f + initialWaterHeight;
			water.setWaterHeight(waterHeight);
		}
	}

	public WaterFilter getWater() {
		return water;
	}
}
