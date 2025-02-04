package net.carriercommander.ui;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This state accepts sub-state classes in the initialize() method. The sub-states get instantiated
 * and attached automatically when this state is initialized. Also when this state gets enabled, it
 * auto enables the last enabled sub-state and disables all sub-states when it gets disabled.
 *
 * A typical use-case is for windows with a set of sub-windows where only one at a time
 * should be visible.
 */
public abstract class ControlState extends WindowState {

	protected List<Class<AppState>> states = new ArrayList<>();
	private Class<AppState> lastActiveSubstate = null;

	@SafeVarargs //FIXME we should use Class<AppState> but this gives a compile error
	protected final void initialize(Application app, Class... subStates) {
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
		super.onEnable();
		switchToState(lastActiveSubstate);
	}

	@Override
	protected void onDisable() {
		window.removeFromParent();
		switchToState(null);
	}

	/**
	 * Enable the specified sub-state and disable all other sub-states
	 * @param classToEnable the class of the state which is to be enabled
	 *                         (must be properly instatiated and initialized via this class' initialize() method)
	 */
	protected void switchToState(Class classToEnable) {
		states.forEach(state -> getState(state).setEnabled(state.equals(classToEnable)));
		if (classToEnable != null) {
			lastActiveSubstate = classToEnable;
		}
	}
}
