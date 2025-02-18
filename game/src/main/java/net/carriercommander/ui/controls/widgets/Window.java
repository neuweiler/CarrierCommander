package net.carriercommander.ui.controls.widgets;

import com.jme3.scene.Spatial;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.core.GuiLayout;
import com.simsilica.lemur.event.CursorEventControl;
import com.simsilica.lemur.event.DragHandler;
import com.simsilica.lemur.style.Attributes;
import com.simsilica.lemur.style.ElementId;
import com.simsilica.lemur.style.StyleDefaults;

public class Window extends Container {

	public static final String ELEMENT_ID = "window";

	public Window() {
		this(0, 0, null, true, null, true, new ElementId(ELEMENT_ID), null);
	}

	public Window(int x, int y) {
		this(x, y, null, true, null, true, new ElementId(ELEMENT_ID), null);
	}

	public Window(int x, int y, String title, boolean draggable) {
		this(x, y, title, draggable, null, true, new ElementId(ELEMENT_ID), null);
	}

	public Window(int x, int y, String title, boolean draggable, GuiLayout layout) {
		this(x, y, title, draggable, layout, true, new ElementId(ELEMENT_ID), null);
	}

	public Window(int x, int y, String title, boolean draggable, String style) {
		this(x, y, title, draggable, null, true, new ElementId(ELEMENT_ID), style);
	}

	public Window(int x, int y, String title, boolean draggable, ElementId elementId) {
		this(x, y, title, draggable, null, true, elementId, null);
	}

	public Window(int x, int y, String title, boolean draggable, ElementId elementId, String style) {
		this(x, y, title, draggable, null, true, elementId, style);
	}

	public Window(int x, int y, String title, boolean draggable, GuiLayout layout, ElementId elementId) {
		this(x, y, title, draggable, layout, true, elementId, null);
	}

	public Window(int x, int y, String title, boolean draggable, GuiLayout layout, String style) {
		this(x, y, title, draggable, layout, true, new ElementId(ELEMENT_ID), style);
	}

	public Window(int x, int y, String title, boolean draggable, GuiLayout layout, ElementId elementId, String style) {
		this(x, y, title, draggable, layout, true, elementId, style);
	}

	protected Window(int x, int y, String title, boolean draggable, GuiLayout layout, boolean applyStyles, ElementId elementId, String style) {
		super(layout, applyStyles, elementId, style);

		setLocalTranslation(x, y, 0);
		addChild(createTitleBar(title, draggable));
	}

	@StyleDefaults(ELEMENT_ID)
	public static void initializeDefaultStyles( Attributes attrs ) {
		Container.initializeDefaultStyles(attrs);
	}

	private Label createTitleBar(String title, boolean draggable) {
		Label label = new Label(title, new ElementId("window.title.label"));

		if (title == null) {
			label.setFontSize(5);
		}

		if (draggable) {
			DragHandler dragHandler = new DragHandler(Spatial::getParent);
			CursorEventControl.addListenersToSpatial(label, dragHandler);
		}

		return label;
	}

}
