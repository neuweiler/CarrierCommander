package net.carriercommander.screen;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import net.carriercommander.objects.PlayerUnit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class PaintedRadar extends PaintableImage {

	public static final int MIN_RANGE = 1000;
	public static final int MAX_RANGE = 10000;

	private final Node rootNode;
	private PlayerUnit activeUnit = null;
	private int range = 3000;
	private int radius, offsetX, offsetY;

	public PaintedRadar(int offsetX, int offsetY, int radius, Node rootNode) {
		super(offsetX + radius, offsetY + radius);
		this.rootNode = rootNode;
		this.radius = radius;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		refreshImage();
	}

	public void setActiveUnit(PlayerUnit activeUnit) {
		this.activeUnit = activeUnit;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public void changeRange(int amount) {
		this.range += amount;
		if (this.range < MIN_RANGE) {
			this.range = MIN_RANGE;
		}
		if (this.range > MAX_RANGE) {
			this.range = MAX_RANGE;
		}
	}

	public void paint(Graphics2D g) {
		g.setBackground(new Color(0f, 0f, 0f, 0f));
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.BLACK);
		g.fill(new Ellipse2D.Double(offsetX - radius, offsetY - radius, radius * 2, radius * 2));

		rootNode.getChildren().forEach(node -> {
			if (activeUnit.getWorldTranslation().distance(node.getWorldTranslation()) <= range) {
				Vector3f radarCoordinates = node.getWorldTranslation().subtract(activeUnit.getWorldTranslation());

				Quaternion radarRotation = activeUnit.getWorldRotation().inverse();
				radarCoordinates = radarRotation.mult(radarCoordinates);
				g.setColor(new Color(0f, 0.51f, 1f, 1f)); // cyan for CC
				g.fillRect((int)(offsetX + radius * radarCoordinates.getX() / range), height - (int)(offsetY + radius * radarCoordinates.z / range), 4, 4);
			}
		});
	}
}
