package com.xydium.rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Draw {

	private static LayerSet layers;
	
	public static void initDraw() {
		layers = new LayerSet();
	}
	
	public static void addDrawingLayer(String layer) {
		layers.addLayer(layer);
	}
	
	public static void setDrawingLayer(String layer) {
		layers.setCurrentLayer(layer);
	}
	
	public static void dumpDrawingLayers() {
		layers.dumpLayers();
	}
	
	public static void clear() {
		layers.cleanLayers();
	}
	
	public static void flatten() {
		layers.compressLayers();
	}
	
	public static BufferedImage getCurrentFrame() {
		return layers.getBuffer();
	}
	
	public static void outlineRect(int x, int y, int width, int height, Color color) {
		graphics().setColor(color);
		graphics().drawRect(x, y, width, height);
	}
	
	public static void outlineCenteredRect(int x, int y, int width, int height, Color color) {
		outlineRect(x - width / 2, y - width / 2, width, height, color);
	}
	
	public static void fillRect(int x, int y, int width, int height, Color color) {
		graphics().setColor(color);
		graphics().fillRect(x, y, width, height);
	}
	
	public static void fillCenteredRect(int x, int y, int width, int height, Color color) {
		fillRect(x - width / 2, y - height / 2, width, height, color);
	}
	
	public static void text(String text, int x, int y, Color color, Font font) {
		graphics().setColor(color);
		graphics().setFont(font);
		graphics().drawString(text, x, y);
	}
	
	public static void centeredText(String text, int x, int y, Color color, Font font) {
		FontMetrics fm = graphics().getFontMetrics(font);
		int cx = x - fm.stringWidth(text) / 2;
		int cy = y + (fm.getAscent() + fm.getDescent()) / 3;
		text(text, cx, cy, color, font);
	}
	
	private static Graphics2D graphics() {
		return layers.getCurrentLayer().graphics();
	}
	
}
