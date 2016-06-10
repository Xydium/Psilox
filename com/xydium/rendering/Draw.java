package com.xydium.rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.xydium.geometry.Vec2;

/**
 * Draw is used to interface with the Layered 2D graphics
 * and allows the user to instruct Psilox what to draw
 * and where. Usually Draw calls will be handled by nodes
 * and setting layers will be handled by Psilox, however
 * all functions are available to be used at any time.
 * Draw calls made outside of the engine's render pass
 * will be cleared and never shown on screen.
 * 
 * @author Xydium
 *
 */
public class Draw {

	private static LayerSet layers;
	
	/**
	 * Called by Psilox to prevent null-pointers
	 * when adding and accessing layers. 
	 */
	public static void initDraw() {
		layers = new LayerSet();
	}
	
	/**
	 * Adds a new layer to the LayerSet with the
	 * given name.
	 * 
	 * @param layer
	 */
	public static void addDrawingLayer(String layer) {
		layers.addLayer(layer);
	}
	
	/**
	 * Sets the current layer of the LayerSet
	 * to the layer with the name that matches
	 * the passed string. Will set layer to
	 * "default" if no valid layer is provided.
	 * 
	 * @param layer
	 */
	public static void setDrawingLayer(String layer) {
		layers.setCurrentLayer(layer);
	}
	
	/**
	 * Removes all drawing layers from the layer set.
	 * Intended for use by Psilox when changing scenes.
	 */
	public static void dumpDrawingLayers() {
		layers.dumpLayers();
	}
	
	/**
	 * Clears all drawn data from layers, making them
	 * completely transparent.
	 */
	public static void clear() {
		layers.cleanLayers();
	}
	
	/**
	 * Takes the multiple layer images and
	 * compresses them into the LayerSet
	 * image buffer.
	 */
	public static void flatten() {
		layers.compressLayers();
	}
	
	/**
	 * To be called by Psilox after performing a flatten call,
	 * returns the current frame buffer with all renderered data
	 * for this frame.
	 * 
	 * @return
	 */
	public static BufferedImage getCurrentFrame() {
		return layers.getBuffer();
	}
	
	/**
	 * Draws the outline of a rectangle with top-left corner
	 * at x,y and extending to bottom-right corner x+width, y+height
	 * with the selected color.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 */
	public static void outlineRect(int x, int y, int width, int height, Color color) {
		graphics().setColor(color);
		graphics().drawRect(x, y, width, height);
	}
	
	public static void outlineRect(Vec2<?> pos, Vec2<?> dim, Color color) {
		outlineRect(pos.getX().intValue(), pos.getY().intValue(), dim.getX().intValue(), dim.getY().intValue(), color);
	}
	
	/**
	 * Draws the outline of a rectangle with the center point
	 * at x,y and dimensions of width and height with the selected
	 * color.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 */
	public static void outlineCenteredRect(int x, int y, int width, int height, Color color) {
		outlineRect(x - width / 2, y - width / 2, width, height, color);
	}
	
	public static void outlineCenteredRect(Vec2<?> pos, Vec2<?> dim, Color color) {
		outlineCenteredRect(pos.getX().intValue(), pos.getY().intValue(), dim.getX().intValue(), dim.getY().intValue(), color);
	}
	
	/**
	 * Fills a rectangle starting at x,y and extending
	 * to x+width, y+height with the selected color.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 */
	public static void fillRect(int x, int y, int width, int height, Color color) {
		graphics().setColor(color);
		graphics().fillRect(x, y, width, height);
	}
	
	public static void fillRect(Vec2<?> pos, Vec2<?> dim, Color color) {
		fillRect(pos.getX().intValue(), pos.getY().intValue(), dim.getX().intValue(), dim.getY().intValue(), color);
	}
	
	/**
	 * Fills a rectangle with center point x,y and
	 * dimensions of width, height with the selected color.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 */
	public static void fillCenteredRect(int x, int y, int width, int height, Color color) {
		fillRect(x - width / 2, y - height / 2, width, height, color);
	}
	
	public static void fillCenteredRect(Vec2<?> pos, Vec2<?> dim, Color color) {
		fillCenteredRect(pos.getX().intValue(), pos.getY().intValue(), dim.getX().intValue(), dim.getY().intValue(), color);
	}
	
	/**
	 * Draws the desired string on screen with
	 * a bottom-left corner at x,y using the chosen
	 * color and font.
	 * 
	 * @param text
	 * @param x
	 * @param y
	 * @param color
	 * @param font
	 */
	public static void text(String text, int x, int y, Color color, Font font) {
		graphics().setColor(color);
		graphics().setFont(font);
		graphics().drawString(text, x, y);
	}
	
	public static void text(String text, Vec2<?> pos, Color color, Font font) {
		text(text, pos.getX().intValue(), pos.getY().intValue(), color, font);
	}
	
	/**
	 * Draws the desired string on screen in a non-visible
	 * rectangle with a center point at x,y using the
	 * chosen color and font.
	 * 
	 * @param text
	 * @param x
	 * @param y
	 * @param color
	 * @param font
	 */
	public static void centeredText(String text, int x, int y, Color color, Font font) {
		FontMetrics fm = graphics().getFontMetrics(font);
		int cx = x - fm.stringWidth(text) / 2;
		int cy = y + fm.getHeight() / 3;
		text(text, cx, cy, color, font);
	}
	
	public static void centeredText(String text, Vec2<?> pos, Color color, Font font) {
		centeredText(text, pos.getX().intValue(), pos.getY().intValue(), color, font);
	}
	
	/**
	 * Gets the GraphicsContext for the current layer
	 * in rendering. Used by Draw to easily access,
	 * however may be used elsewhere for direct
	 * access to Graphics2D functions that have not
	 * been wrapped.
	 * 
	 * @return
	 */
	public static Graphics2D graphics() {
		return layers.getCurrentLayer().graphics();
	}
	
}
