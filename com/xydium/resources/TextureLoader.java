package com.xydium.resources;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.xydium.utility.Log;

public class TextureLoader {

	private static final ClassLoader LOADER = TextureLoader.class.getClassLoader();
	
	public static BufferedImage loadTexture(String texturePath) {
		try {
			return ImageIO.read(LOADER.getResourceAsStream(texturePath));
		} catch (IOException e) {
			Log.error("Failed to load image " + texturePath);
			return null;
		}
	}
	
}
