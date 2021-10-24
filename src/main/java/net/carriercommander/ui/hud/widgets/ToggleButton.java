package net.carriercommander.ui.hud.widgets;

import com.simsilica.lemur.Action;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.style.ElementId;

public class ToggleButton extends ActionButton {
	public static final String ELEMENT_ID = "toggleButton";
	private boolean selected;

	public ToggleButton(Action action) {
		this(action, new ElementId(ELEMENT_ID), null);
	}

	public ToggleButton(Action action, String style) {
		this(action, new ElementId(ELEMENT_ID), style);
	}

	public ToggleButton(Action action, ElementId elementId) {
		this(action, elementId, null);
	}

	public ToggleButton(Action action, ElementId elementId, String style) {
		super(action, elementId, style);
	}

	@Override
	protected void runClick() {
		if( !isEnabled() )
			return;
		selected = !selected;
		super.runClick();
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
