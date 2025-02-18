package net.carriercommander.ui;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import net.carriercommander.ui.controls.widgets.Window;

/**
 * A state class for windows which attach/detach automatically from the GUI node when enabled/disabled.
 * If used, the variable "window" must be set in the initialize() phase of the subclass.
 */
public abstract class WindowState extends AbstractState {

	protected Window window;

	/**
	 * Scale content of window and position relatively on screen according to scale and
	 * screen resolution.
	 *
	 * @param camera the camera object holding the screen resolution
	 * @param xPercent the relative x position of the window (0 = left border, 0.5f = center, 1 = right border)
	 * @param yPercent the relative y position of the window (0 = bottom, 0.5f = center, 1 = top)
	 */
	protected void scaleAndPosition(Camera camera, float xPercent, float yPercent) {
		scaleAndPosition(camera, xPercent, yPercent, 1.0f);
	}

	/**
	 * Scale content of window and position relatively on screen according to scale and
	 * screen resolution.
	 *
	 * @param camera the camera object holding the screen resolution
	 * @param xPercent the relative x position of the window (0 = left border, 0.5f = center, 1 = right border)
	 * @param yPercent the relative y position of the window (0 = bottom, 0.5f = center, 1 = top)
	 * @param magnification the magnification factor to use (1 = scale according to resolution)
	 */
	protected void scaleAndPosition(Camera camera, float xPercent, float yPercent, float magnification) {
		Vector3f preferredSize = window.getPreferredSize().clone();
		float standardScale = getStandardScale();
		preferredSize.multLocal(standardScale * magnification);

		float x = Math.max(camera.getWidth() * xPercent - preferredSize.x * 0.5f, 0);
		x = Math.min(x, camera.getWidth() - preferredSize.x);
		float y = Math.max(camera.getHeight() * yPercent + preferredSize.y * 0.5f, preferredSize.y);
		y = Math.min(y, camera.getHeight());

		window.setLocalTranslation(x, y, 0);
		window.setLocalScale(standardScale * magnification);
	}

	@Override
	protected void onEnable() {
		Node gui = ((SimpleApplication) getApplication()).getGuiNode();
		gui.attachChild(window);
	}

	@Override
	protected void onDisable() {
		if (window != null) {
			window.removeFromParent();
		}
	}
}
