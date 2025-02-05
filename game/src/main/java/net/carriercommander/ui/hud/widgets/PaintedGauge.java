package net.carriercommander.ui.hud.widgets;

import java.awt.Color;
import java.awt.Graphics2D;

public class PaintedGauge extends PaintableImage {
	private final int widthGauge, heightGauge, offsetX, offsetY;
	private final boolean vertical;
	private float value = 0;

	private final Color colorGauge = new Color(0f, 0.51f, 1f, 1f);
	private final Color colorBackground = new Color(0f, 0f, 0f, 0.5f);

	/**
	 * A 2D painted gauge which can be inserted into a nifty gui image. Make sure that where the gauge should appear
	 * the host image is not fully opaque - the gauge will be rendered behind the image.
	 *
	 * @param widthContainer The width of the container image - should match the picture width otherwise gauge will get stretched
	 * @param heightContainer The height of the container image - should match the picture height otherwise gauge will get stretched
	 * @param offsetX The x offset where the start point of the gauge will appear inside the host image
	 * @param offsetY The y offset where the start point of the gauge will appear inside the host image
	 * @param widthGauge The desired width of the gauge bar
	 * @param heightGauge The desired height of the gauge bar
	 * @param vertical if true, the gauge will be displayed vertically, otherwise horizontally
	 */
	public PaintedGauge(int widthContainer, int heightContainer, int offsetX, int offsetY, int widthGauge, int heightGauge, boolean vertical) {
		super(widthContainer, heightContainer);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.widthGauge = widthGauge;
		this.heightGauge = heightGauge;
		this.vertical = vertical;
		refreshImage();
	}

	/**
	 * Create a painted gauge with reasonable settings for this game.
	 */
	public PaintedGauge() {
		this(42, 90, 30, 7, 4, 74, true);
	}

	/**
	 * Set the value to be displayed by the gauge.
	 *
	 * @param value between 0 and 1
	 */
	public void setValue(float value) {
		if (value > 1) {
			value = 1;
		}
		if (value < 0) {
			value = 0;
		}
		if (value != this.value) {
			this.value = value;
			refreshImage();
		}
	}

	/**
	 * Paint the gauge with the defined parameters and value.
	 *
	 * @param g the graphics object to paint on - provided by PaintableImage.refreshImage().
	 */
	@Override
	protected void paint(Graphics2D g) {
		g.setBackground(colorBackground);
		g.clearRect(offsetX, offsetY, widthGauge, heightGauge);
		g.setColor(colorGauge);
		if (vertical) {
			g.fillRect(offsetX, offsetY, widthGauge, Math.round(value * heightGauge));
		} else {
			g.fillRect(offsetX, offsetY, Math.round(value * widthGauge), heightGauge);
		}
	}
}
