package com.xydium.rendering;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.xydium.core.Psilox;

public class Layer {

	private BufferedImage image;
	private Graphics2D graphics;
	
	public Layer() {
		image = new BufferedImage(Psilox.windowWidth(), Psilox.windowHeight(), BufferedImage.TYPE_INT_ARGB);
		graphics = image.createGraphics();
	}
	
	public BufferedImage image() {
		return image;
	}
	
	public Graphics2D graphics() {
		return graphics;
	}
	
}
