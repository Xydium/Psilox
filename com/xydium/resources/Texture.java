package com.xydium.resources;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Texture {
	
	private BufferedImage image;
	private Graphics2D graphics;
	
	public Texture(BufferedImage image) {
		setImage(image);
	}
	
	public Texture(String texturePath) {
		this(TextureLoader.loadTexture(texturePath));
	}
	
	public BufferedImage getImage() {
		return image;
	}
	
	public BufferedImage setImage(BufferedImage newImage) {
		BufferedImage oldImage = image;
		image = newImage;
		graphics = image.createGraphics();
		return oldImage;
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
	public Graphics2D graphics() {
		return graphics;
	}
	
	public void setPixel(int x, int y, Color color) {
		if(x < 0 || x > getWidth() || y < 0 || y > getHeight()) return;
		image.setRGB(x, y, color.getRGB());
	}
	
	public static Texture create(int width, int height) {
		return new Texture(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
	}
	
}
