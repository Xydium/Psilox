package com.xydium.resources;

import java.awt.image.BufferedImage;

public class Texture {
	
	private BufferedImage image;
	
	public Texture(BufferedImage image) {
		this.image = image;
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
		return oldImage;
	}
	
	public int getWidth() {
		return image.getWidth();
	}
	
	public int getHeight() {
		return image.getHeight();
	}
	
}
