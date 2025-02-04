package net.carriercommander.ui.hud.widgets;

import com.simsilica.lemur.CallMethodAction;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.style.ElementId;

public class ToggleImageButton extends ToggleButton {

	public static final String ELEMENT_ID = "toggleImageButton";

	public ToggleImageButton(String imageFile, Object object, String method) {
		this(imageFile, object, method, new ElementId(ELEMENT_ID), null);
	}

	public ToggleImageButton(String imageFile, Object object, String method, String style) {
		this(imageFile, object, method, new ElementId(ELEMENT_ID), style);
	}

	public ToggleImageButton(String imageFile, Object object, String method, ElementId elementId) {
		this(imageFile, object, method, elementId, null);
	}

	public ToggleImageButton(String imageFile, Object object, String method, ElementId elementId, String style) {
		super(new CallMethodAction(null, object, method), elementId, style);
		setIcon(new IconComponent(imageFile));
	}
}
