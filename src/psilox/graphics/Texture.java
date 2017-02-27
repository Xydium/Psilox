package psilox.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import psilox.utils.BufferUtils;

public class Texture {
	
	private int width, height;
	private int texture;
	private int frameBuffer;
	
	public Texture(String path) {
		load(path);
	}
	
	public Texture(BufferedImage image) {
		load(image);
	}
	
	public Texture(int width, int height) {
		this.width = width;
		this.height = height;
		create(null);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getTexture() {
		return texture;
	}
	
	public int getFrameBuffer() {
		return frameBuffer;
	}
	
	void setFrameBuffer(int buffer) {
		this.frameBuffer = buffer;
	}
	
	private void load(String path) {
		int[] pixels = null;
		try {
			BufferedImage image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int[] data = new int[width * height];
		for (int i = 0; i < width * height; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		create(data);
	}
	
	private void load(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		
		int[] data = new int[width * height];
		for (int i = 0; i < width * height; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		create(data);
	}
	
	public void setData(BufferedImage image) {
		bind();
		int dataWidth = image.getWidth();
		int dataHeight = image.getHeight();
		
		int newWidth = Math.max(dataWidth, width);
		int newHeight = Math.max(dataHeight, height);
		
		int[] pixels = new int[newWidth * newHeight];
		image.getRGB(0, 0, dataWidth, dataHeight, pixels, 0, newWidth);
		int[] data = new int[pixels.length];
		for (int i = 0; i < data.length; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		if(newWidth > width || newHeight > height) {
			width = newWidth; height = newHeight;
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
		} else {
			width = newWidth; height = newHeight;
			glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, data);
		}
		unbind();
	}
	
	private void create(int[] data) {
		texture = glGenTextures();
		bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		if(data == null) {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
		} else {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
		}
		unbind();
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texture);
	}
	
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public void dispose() {
		glDeleteTextures(texture);
	}

}