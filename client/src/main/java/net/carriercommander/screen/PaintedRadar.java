package net.carriercommander.screen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class PaintedRadar extends PaintableImage {

	public PaintedRadar(int width, int height) {
		super(width, height);
		refreshImage();
	}

	public void paint(Graphics2D g) {
		g.setBackground(new Color(0f, 0f, 0f, 0f));
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		g.fill(new Ellipse2D.Double(2, 6, 79, 79));

		//TODO get object positions relative to carrier and paint a dot for each one, also paint island
		g.setColor(new Color(0f, 0.51f, 1f, 1f)); // cyan for CC
		g.fillRect(8, 40, (int)Math.round(Math.random() * (getWidth() - 20)), 5);
	}
}
