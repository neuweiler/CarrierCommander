package net.carriercommander.ui.hud.carrier;

import com.jme3.app.Application;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import net.carriercommander.Constants;
import net.carriercommander.Player;
import net.carriercommander.StatePlayer;
import net.carriercommander.objects.Carrier;
import net.carriercommander.objects.PlayerItem;
import net.carriercommander.objects.resources.ResourceContainer;
import net.carriercommander.ui.WindowState;
import net.carriercommander.ui.hud.widgets.ImageButton;
import net.carriercommander.ui.hud.widgets.Window;

public class StateResources extends WindowState {
	public static final String KEY_INDEX = "index";
	private StateResourceEditor resourceEditor = null;

	@Override
	protected void initialize(Application app) {
		window = new Window();
		Carrier carrier = (Carrier) getPlayer().getItem(Constants.CARRIER);
		Container row = window.addChild(new Container());
		int i = 0, x = 1;
		for (ResourceContainer resourceContainer : carrier.getResourceManager().getContainers()) {
			row.addChild(new ImageButton("/Interface/hud/carrier/resources/" + resourceContainer.getItem().getIconFileName(), this, "selectResource"), x++)
					.setUserData(KEY_INDEX, i++);
			if (x > 5) {
				row = window.addChild(new Container());
				x = 1;
			}
		}
		scaleAndPosition(app.getCamera(), .25f, .6f);

		resourceEditor = new StateResourceEditor(getPlayer());
		getStateManager().attach(resourceEditor);
		resourceEditor.setEnabled(false);
	}

	@Override
	protected void cleanup(Application app) {
	}

	private Player getPlayer() {
		StatePlayer statePlayer = getState(StatePlayer.class);
		if (statePlayer == null) {
			throw new IllegalStateException("player state not initialized");
		}
		return statePlayer.getPlayer();
	}

	private void selectResource(Button button) {
		int index = button.getUserData(KEY_INDEX);
		resourceEditor.selectResource(index);
	}
}
