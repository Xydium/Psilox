package psilox.node;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import psilox.graphics.Color;
import psilox.graphics.Texture;
import psilox.math.Vec;

public class Label extends Node {
	
	private static final Graphics GRAPHICS = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
	
	private String text;
	private Color color;
	private Font font;
	private FontMetrics metrics;
	
	public Label(String tag, Color color, Font font, String text) {
		super(tag);
		this.text = text;
		this.color = color;
		this.font = font;
		this.metrics = GRAPHICS.getFontMetrics(font);
		this.texture = new Texture(1, 1);
		drawText();
	}
	
	private void drawText() {
		int width = metrics.stringWidth(text);
		int height = metrics.getHeight();
		BufferedImage image = new BufferedImage(width == 0 ? 1 : width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		g.setFont(font);
		g.setColor(new java.awt.Color(color.r, color.g, color.b, color.a));
		g.drawString(text, 0, image.getHeight() - metrics.getMaxDescent());
		texture.setData(image);
		this.dimensions = new Vec(texture.getWidth(), texture.getHeight());
	}
	
}
