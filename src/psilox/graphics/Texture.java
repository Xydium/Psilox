package psilox.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import psilox.math.Vec;
import psilox.utility.Buffers;

public class Texture {
	
	private int width, height;
	private int texture;
	private int frameBuffer;

	/**
	 * Loads an image file from the resource
	 * path and transfers it into VRAM.
	 * 
	 * @param path
	 */
	public Texture(String path) {
		load(path);
	}
	
	/**
	 * Transfers the data from the image passed
	 * to VRAM.
	 * 
	 * @param image
	 */
	public Texture(BufferedImage image) {
		load(image);
	}
	
	/**
	 * Generates and allocates empty VRAM
	 * of the given dimensions.
	 * 
	 * @param width
	 * @param height
	 */
	public Texture(int width, int height) {
		this.width = width;
		this.height = height;
		create(null);
	}
	
	/**
	 * Generates and allocates empty VRAM
	 * of the given dimensions.
	 * 
	 * @param dim
	 */
	public Texture(Vec dim) {
		this((int) dim.x, (int) dim.y);
	}
	
	/**
	 * @return pixel width of texture
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * @return pixel height of texture
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * @return pixel dimensions of texture
	 */
	public Vec getDimensions() {
		return new Vec(width, height);
	}
	
	/**
	 * @return VRAM handle of texture
	 */
	public int getTexture() {
		return texture;
	}
	
	/**
	 * @return VRAM handle of framebuffer
	 */
	public int getFrameBuffer() {
		return frameBuffer;
	}

	public void setFrameBuffer(int buffer) {
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
	
	/**
	 * Replaces the data in VRAM with the data
	 * of the given image.
	 * 
	 * @param image
	 */
	public void setData(BufferedImage image) {
		width = image.getWidth();
		height = image.getHeight();
		
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, width);
		setData(pixels, width, height);
	}
	
	/**
	 * Replaces the data of the texture in VRAM
	 * with the given int data.
	 * 
	 * @param pixels
	 * @param width
	 * @param height
	 */
	public void setData(int[] pixels, int width, int height) {
		this.width = width;
		this.height = height;
		int a, r, g, b;
		for (int i = 0; i < pixels.length; i++) {
			a = (pixels[i] & 0xff000000) >> 24;
			r = (pixels[i] & 0xff0000) >> 16;
			g = (pixels[i] & 0xff00) >> 8;
			b = (pixels[i] & 0xff);
			
			pixels[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		bind();
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, Buffers.createIntBuffer(pixels));
		unbind();
	}
	
	/**
	 * Sets the data of the texture in VRAM
	 * to the int data passed without converting it
	 * to the reversed color format.
	 * 
	 * @param data
	 * @param width
	 * @param height
	 */
	public void rawWrite(int[] data, int width, int height) {
		this.width = width;
		this.height = height;
		bind();
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, Buffers.createIntBuffer(data));
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
	
	/**
	 * Makes the texture available for rendering.
	 */
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, texture);
	}
	
	/**
	 * Makes the texture unavailable for rendering.
	 */
	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	/**
	 * Deletes the texture from VRAM.
	 */
	public void dispose() {
		glDeleteTextures(texture);
	}

}
