package net.carriercommander.ui.menu;

import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.control.CameraControl;
import com.simsilica.lemur.*;
import net.carriercommander.*;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.hud.StateMainControls;

public class StateLoadGame extends WindowState {
	private int loadPart = 0;
	private ProgressBar progressBar;
	private Label statusText;

	private CameraNode camNode = null;

	private StateTerrain stateTerrain;
	private StateSky stateSky;
	private StateLights stateLights;
	private StateWater stateWater;
	private StateViewFilter stateViewFilter;
	private StatePlayer statePlayer;
	private StateMainControls stateMainControls;

	protected void initialize(Application app) {
		window = new Container();

		Label title = window.addChild(new Label("Starting Game"));
		title.setFontSize(24);
		title.setInsets(new Insets3f(10, 10, 0, 10));

		progressBar = window.addChild(new ProgressBar());
		statusText = window.addChild(new Label(""));
		statusText.setFontSize(12);

		int height = app.getCamera().getHeight();
		Vector3f pref = window.getPreferredSize().clone();

		float standardScale = getStandardScale();
		pref.multLocal(1.5f * standardScale);

		float y = height * 0.6f + pref.y * 0.5f;

		window.setLocalTranslation(100 * standardScale, y, 0);
		window.setLocalScale(1.5f * standardScale);

		configureCamera();

		stateTerrain = new StateTerrain();
		stateSky = new StateSky();
		stateLights =new StateLights();
		stateWater = new StateWater();
		stateViewFilter = new StateViewFilter();
		statePlayer = new StatePlayer(camNode);
		stateMainControls = new StateMainControls();
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		loadPart = 0;
	}

	@Override
	protected void onDisable() {
		super.onDisable();
	}

	@Override
	public void update(float tpf) {
		load();
	}

	private void load() {
		switch (loadPart) {
			case 0:
				setProgress(0.2f, "creating terrain");
				getStateManager().attach(stateTerrain);
				break;
			case 1:
				setProgress(0.3f, "creating sky");
				getStateManager().attach(stateSky);
				break;
			case 2:
				setProgress(0.4f, "creating lights");
				getStateManager().attach(stateLights);
				break;
			case 3:
				setProgress(0.5f, "creating water");
				getStateManager().attach(stateWater);
				break;
			case 4:
				setProgress(0.6f, "adding post process filter");
				getStateManager().attach(stateViewFilter);
				break;
			case 5:
				setProgress(0.7f, "creating objects");
				getStateManager().attach(statePlayer);
				break;
			case 6:
				setProgress(1.0f, "finished");
				getStateManager().attach(stateMainControls);
				getStateManager().detach(this);
				getStateManager().detach(getState(StateSpinningCarrier.class));
				break;
		}
		loadPart++;
	}

	private void setProgress(float progress, String text) {
		progressBar.setProgressPercent(progress);
		progressBar.setMessage(Math.round(progress * 100) + "%");
		statusText.setText(text);
	}

	private void configureCamera() {
		camNode = new CameraNode("Camera Node", getApplication().getCamera());
		camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
		getApplication().getCamera().setFrustumFar(20000);
	}

}
