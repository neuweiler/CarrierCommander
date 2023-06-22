package net.carriercommander.ui.menu;

import com.simsilica.lemur.Button;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.ValueRenderer;
import com.simsilica.lemur.style.ElementId;
import net.carriercommander.network.model.config.GameConfig;

public class GameConfigCellRenderer implements ValueRenderer {
	ElementId elementId;
	String style;

	@Override
	public void configureStyle(ElementId elementId, String style) {
		if (this.elementId == null) {
			this.elementId = elementId;
		}
		if (this.style == null) {
			this.style = style;
		}
	}

	@Override
	public Panel getView(Object value, boolean selected, Panel existing) {
		if (existing == null) {
			existing = new Button(((GameConfig) value).getName(), elementId, style);
		} else {
			((Button) existing).setText(((GameConfig) value).getName());
		}
		return existing;
	}
}
