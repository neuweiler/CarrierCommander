package net.carriercommander.ui.controls.widgets;

import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.CallMethodAction;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.style.ElementId;

public class ImageButton extends ActionButton {

	public static final String ELEMENT_ID = "imageButton";

	public ImageButton(String imageFile, Object object, String method) {
		this(imageFile, object, method, new ElementId(ELEMENT_ID), null);
	}

	public ImageButton(String imageFile, Object object, String method, String style) {
		this(imageFile, object, method, new ElementId(ELEMENT_ID), style);
	}

	public ImageButton(String imageFile, Object object, String method, ElementId elementId) {
		this(imageFile, object, method, elementId, null);
	}

	public ImageButton(String imageFile, Object object, String method, ElementId elementId, String style) {
		super(new CallMethodAction(null, object, method), elementId, style);
		setIcon(new IconComponent(imageFile));
	}
}
