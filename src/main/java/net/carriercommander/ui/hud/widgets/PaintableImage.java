package net.carriercommander.ui.hud.widgets;

import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public abstract class PaintableImage extends Image {
	private BufferedImage bufferedImage;
	private ByteBuffer scratch;
	Texture2D texture;

	public PaintableImage(int width, int height) {
		super();

		setFormat(Format.RGBA8);
		setWidth(width);
		setHeight(height);

		bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR_PRE);
		scratch = ByteBuffer.allocateDirect(4 * width * height);

		texture = new Texture2D(this);
		texture.setMinFilter(Texture.MinFilter.Trilinear);
		texture.setMagFilter(Texture.MagFilter.Bilinear);
	}

	public void refreshImage() {
		Graphics2D g = bufferedImage.createGraphics();
		paint(g);
		g.dispose();

		/* get the image data */
		byte data[] = (byte[]) bufferedImage.getRaster().getDataElements(0, 0, getWidth(), getHeight(), null);
		scratch.clear();
		scratch.put(data, 0, data.length);
		scratch.rewind();
		setData(scratch);
	}

	public Texture getTexture() {
		return texture;
	}

	protected abstract void paint(Graphics2D graphicsContext);
}

