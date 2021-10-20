package net.carriercommander.ui;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppState;
import com.jme3.scene.Node;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.CallMethodAction;
import com.simsilica.lemur.component.IconComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ControlState extends WindowState {

	protected List<Class<AppState>> states = new ArrayList<>();
	private Class<AppState> lastActiveSubstate = null;

	@SafeVarargs //FIXME we should use Class<AppState> but this gives a compile error
	protected final void initialize(Application app, Class... subStates) {
		super.initialize(app);

		states = Arrays.asList(subStates);
		states.forEach(state -> {
			try {
				getStateManager().attach(state.getDeclaredConstructor().newInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		switchToState(null);
		lastActiveSubstate = states.get(0);
	}

	@Override
	protected void cleanup(Application app) {
		states.forEach(state -> getStateManager().detach(getState(state)));
	}

	@Override
	protected void onEnable() {
		Node gui = ((SimpleApplication) getApplication()).getGuiNode();
		gui.attachChild(window);
		switchToState(lastActiveSubstate);
	}

	@Override
	protected void onDisable() {
		window.removeFromParent();
		switchToState(null);
	}

	protected void switchToState(Class classToEnable) {
		states.forEach(state -> getState(state).setEnabled(state.equals(classToEnable)));
		if (classToEnable != null) {
			lastActiveSubstate = classToEnable;
		}
	}
}
