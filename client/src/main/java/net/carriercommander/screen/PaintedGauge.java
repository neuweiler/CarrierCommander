package net.carriercommander.screen;

import java.awt.Color;
import java.awt.Graphics2D;

public class PaintedGauge extends PaintableImage {
	private static final int MAXIMUM = 100;
	private float value = 0;

	public PaintedGauge(int width, int height) {
		super(width, height);
		refreshImage();
	}

	public void setValue(float newValue) {
		newValue %= MAXIMUM;
		if (newValue != value) {
			value = newValue;
			refreshImage();
		}
	}

	public void paint(Graphics2D g) {
		g.setBackground(new Color(0f, 0f, 0f, 0f));
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(new Color(0f, 0.51f, 1f, 1f)); // cyan for CC
		g.fillRect(0, 0, Math.round(value * getWidth() / MAXIMUM), getHeight());
	}
}
