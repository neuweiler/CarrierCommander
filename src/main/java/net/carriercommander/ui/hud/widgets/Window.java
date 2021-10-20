package net.carriercommander.ui.hud.widgets;

import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.event.CursorEventControl;
import com.simsilica.lemur.event.DragHandler;
import com.simsilica.lemur.style.ElementId;

public class Window extends Container {

	private Label titleBar;

	public Window(int x, int y) {
		this(x, y, null, true);
	}

	public Window(int x, int y, String title, boolean draggable) {
		setLocalTranslation(x, y, 0);

		titleBar = createTitleBar(title, draggable);
		addChild(titleBar);
	}

	private Label createTitleBar(String title, boolean draggable) {
		Label label = new Label(title, new ElementId("window.title.label"));

		if (title == null) {
			label.setFontSize(5);
		}

		if (draggable) {
			DragHandler dragHandler = new DragHandler();
			dragHandler.setDraggableLocator(spatial -> spatial.getParent());
			CursorEventControl.addListenersToSpatial(label, dragHandler);
		}

		return label;
	}

}
