package net.carriercommander.screen;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import net.carriercommander.objects.PlayerUnit;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class PaintedRadar extends PaintableImage {

	public static final int MIN_RANGE = 1000;
	public static final int MAX_RANGE = 10000;

	private final int offsetX, offsetY, radius;
	private final Ellipse2D radarCircle;
	private final Node rootNode;
	private PlayerUnit activeUnit = null;
	private int range = 3000;

	private final Color colorBackground = new Color(0f, 0f, 0f, 0f);
	private final Color colorCircle = Color.BLACK;
	private final Color colorPlayer = new Color(0f, 0.51f, 1f, 1f); // cyan for CC

	/**
	 * A 2D painted radar which can be inserted into a nifty gui image. Make sure that where the radar should appear
	 * the host image is not fully opaque - the gauge will be rendered behind the image.
	 *
	 * @param widthContainer The width of the container image - should match the picture width otherwise radar will get stretched
	 * @param heightContainer The heigth of the container image - should match the picture heigth otherwise radar will get stretched
	 * @param offsetX The x offset where the start point of the gauge will appear inside the host image
	 * @param offsetY The y offset where the start point of the gauge will appear inside the host image
	 * @param radius The radius of the radar to be painted
	 * @param rootNode The node whose children should be painted on the radar screen
	 */
	public PaintedRadar(int widthContainer, int heightContainer, int offsetX, int offsetY, int radius, Node rootNode) {
		super(widthContainer, heightContainer);
		this.rootNode = rootNode;
		this.radius = radius;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		radarCircle = new Ellipse2D.Double(offsetX - radius, offsetY - radius, radius * 2, radius * 2);
		refreshImage();
	}

	public void setActiveUnit(PlayerUnit activeUnit) {
		this.activeUnit = activeUnit;
	}

	public void setRange(int range) {
		if (range < MIN_RANGE) {
			range = MIN_RANGE;
		}
		if (range > MAX_RANGE) {
			range = MAX_RANGE;
		}
		if (range != this.range) {
			this.range = range;
			refreshImage();
		}
	}

	public void changeRange(int amount) {
		setRange(range + amount);
	}

	protected void paint(Graphics2D g) {
		g.setBackground(colorBackground);
		g.clearRect(0, 0, width, height);
		g.setColor(colorCircle);
		g.fill(radarCircle);

		rootNode.getChildren().stream()
				.filter(node -> !node.getClass().equals(Geometry.class))
				.forEach(node -> {
					if (activeUnit.getWorldTranslation().distance(node.getWorldTranslation()) <= range) {
						Vector3f radarCoordinates = node.getWorldTranslation().subtract(activeUnit.getWorldTranslation());

						Quaternion radarRotation = activeUnit.getWorldRotation().inverse();
						radarCoordinates = radarRotation.mult(radarCoordinates);
						g.setColor(colorPlayer);
						g.fillRect((int) (offsetX + radius * radarCoordinates.x / range), height - (int) (offsetY + radius * radarCoordinates.z / range), 2, 2);
					}
				});
	}
}
