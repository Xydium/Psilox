package com.xydium.rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.xydium.geometry.Transform;
import com.xydium.geometry.Vec2;
import com.xydium.geometry.Vec2f;
import com.xydium.geometry.Vec2i;
import com.xydium.resources.Texture;

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
	private static Texture temporaryTarget;
	
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
	
	public static void bindTemporaryTarget(Texture texture) {
		temporaryTarget = texture;
	}
	
	public static void releaseTemporaryTarget() {
		temporaryTarget = null;
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
	
	public static void texture(Texture texture, int x, int y) {
		texture(texture, x, y, 1.0);
	}
	
	public static void texture(Texture texture, int x, int y, double scale) {
		texture(texture, x, y, (int) (texture.getWidth() * scale), (int) (texture.getHeight() * scale));
	}
	
	public static void texture(Texture texture, int x, int y, int width, int height) {
		graphics().drawImage(texture.getImage(), x, y, width, height, null);
	}
	
	public static void texture(Texture texture, Vec2<?> pos) {
		texture(texture, pos, 1.0);
	}
	
	public static void texture(Texture texture, Vec2<?> pos, double scale) {
		texture(texture, pos, new Vec2f(texture.getWidth() * (float) scale, texture.getHeight() * (float) scale));
	}
	
	public static void texture(Texture texture, Vec2<?> pos, Vec2<?> dim) {
		texture(texture, pos.getX().intValue(), pos.getY().intValue(), dim.getX().intValue(), dim.getY().intValue());
	}
	
	public static void texture(Texture texture, int x, int y, int theta) { 
		texture(texture, x, y, 1.0, theta);
	}
	
	public static void texture(Texture texture, int x, int y, double scale, int theta) {
		texture(texture, x, y, (int) (texture.getWidth() * scale), (int) (texture.getHeight() * scale), theta);
	}
	
	public static void texture(Texture texture, int x, int y, int width, int height, int theta) {
		Graphics2D g = (Graphics2D) graphics().create();
		g.rotate(Math.toRadians(theta), x, y);
		g.drawImage(texture.getImage(), x - width / 2, y - height / 2, width, height, null);
		g.dispose();
	}
	
	public static void texture(Texture texture, Vec2<?> pos, int theta) {
		texture(texture, pos, new Vec2i(texture.getWidth(), texture.getHeight()), theta);
	}
	
	public static void texture(Texture texture, Vec2<?> pos, double scale, int theta) {
		texture(texture, pos, new Vec2f(texture.getWidth() * (float) scale, texture.getHeight() * (float) scale), theta);
	}
	
	public static void texture(Texture texture, Vec2<?> pos, Vec2<?> dim, int theta) {
		texture(texture, pos.getX().intValue(), pos.getY().intValue(), dim.getX().intValue(), dim.getY().intValue(), theta);
	}
	
	public static void texture(Texture texture, Transform transform) {
		texture(texture, transform.positionGlobal(), new Vec2f((float) texture.getWidth(), (float) texture.getHeight()).mul(transform.scaleGlobal()), (int) transform.rotationGlobal());
	}
	
	public static void line(int ox, int oy, int ex, int ey, Color color) {
		graphics().setColor(color);
		graphics().drawLine(ox, oy, ex, ey);
	}
	
	public static void line(Vec2<?> origin, Vec2<?> end, Color color) {
		line(origin.getX().intValue(), origin.getY().intValue(), end.getX().intValue(), end.getY().intValue(), color);
	}
	
	public static void outlineOval(int x, int y, int width, int height, Color color) {
		graphics().setColor(color);
		graphics().drawOval(x, y, width, height);
	}
	
	public static void outlineOval(Vec2<?> pos, Vec2<?> dim, Color color) {
		outlineOval(pos.getX().intValue(), pos.getY().intValue(), dim.getX().intValue(), dim.getY().intValue(), color);
	}
	
	public static void outlineCenteredOval(int x, int y, int width, int height, Color color) {
		outlineOval(x - width / 2, y - height / 2, width, height, color);
	}
	
	public static void outlineCenteredOval(Vec2<?> pos, Vec2<?> dim, Color color) {
		outlineCenteredOval(pos.getX().intValue(), pos.getY().intValue(), dim.getX().intValue(), dim.getY().intValue(), color);
	}
	
	public static void fillOval(int x, int y, int width, int height, Color color) {
		graphics().setColor(color);
		graphics().fillOval(x, y, width, height);
	}
	
	public static void fillOval(Vec2<?> pos, Vec2<?> dim, Color color) {
		fillOval(pos.getX().intValue(), pos.getY().intValue(), dim.getX().intValue(), dim.getY().intValue(), color);
	}
	
	public static void fillCenteredOval(int x, int y, int width, int height, Color color) {
		fillOval(x - width / 2, y - height / 2, width, height, color);
	}
	
	public static void fillCenteredOval(Vec2<?> pos, Vec2<?> dim, Color color) {
		fillCenteredOval(pos.getX().intValue(), pos.getY().intValue(), dim.getX().intValue(), dim.getY().intValue(), color);
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
		if(temporaryTarget != null) {
			return temporaryTarget.graphics();
		}
		return layers.getCurrentLayer().graphics();
	}
	
}
