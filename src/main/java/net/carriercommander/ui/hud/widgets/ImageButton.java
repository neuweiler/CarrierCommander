package net.carriercommander.ui.hud.widgets;

import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.CallMethodAction;
import com.simsilica.lemur.component.IconComponent;

public class ImageButton extends ActionButton {

	public ImageButton(String imageFile, Object object, String method) {
		super(new CallMethodAction(null, object, method));
		setIcon(new IconComponent(imageFile));
	}
}
