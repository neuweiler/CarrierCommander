package net.carriercommander.ui.menu;

import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.control.CameraControl;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.ProgressBar;
import net.carriercommander.Constants;
import net.carriercommander.StateLights;
import net.carriercommander.StatePlayer;
import net.carriercommander.StateSky;
import net.carriercommander.StateSpinningCarrier;
import net.carriercommander.StateTerrain;
import net.carriercommander.StateViewFilter;
import net.carriercommander.StateWater;
import net.carriercommander.network.model.config.Island;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.controls.StateMainControls;
import net.carriercommander.ui.controls.widgets.Window;

import java.util.List;

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
	private final Vector3f startPosition;
	private final List<Island> islands;
	private final List<List<String>> connections;

	public StateLoadGame(Vector3f startPosition, List<Island> islands, List<List<String>> connections) {
		this.startPosition = startPosition;
		this.islands = islands;
		this.connections = connections;
	}

	protected void initialize(Application app) {
		window = new Window();

		Label title = window.addChild(new Label("Starting Game"));
		title.setFontSize(24);
		title.setInsets(new Insets3f(10, 10, 0, 10));

		progressBar = window.addChild(new ProgressBar());
		statusText = window.addChild(new Label(""));
		statusText.setFontSize(12);

		scaleAndPosition(app.getCamera(), .3f, .6f, Constants.MENU_MAGNIFICATION);

		configureCamera();

		stateTerrain = new StateTerrain(islands);
		stateSky = new StateSky();
		stateLights =new StateLights();
		stateWater = new StateWater();
		stateViewFilter = new StateViewFilter();
		statePlayer = new StatePlayer(camNode, startPosition);
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
