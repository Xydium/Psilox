package com.xydium.resources;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.xydium.utility.Log;

public class FontLib {

	private static Map<String, Font> fontLib = new HashMap<String, Font>();

	public static void register(String filePath) {
		try {
			InputStream stream = FontLib.class.getClassLoader().getResourceAsStream(filePath);
			Font fnt = Font.createFont(Font.TRUETYPE_FONT, stream);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(fnt);
		} catch(FontFormatException e1) {
			Log.error("Invalid ttf file: " + filePath);
			Log.error(e1);
		} catch(IOException e) {
			Log.error("Non-existant ttf file: " + filePath);
			Log.error(e);
		}
	}
	
	/**
	 * Returns a font object of the requested
	 * font, size, and style if fontData is
	 * passed in the form "Fontname_32_B".
	 * 
	 * @param fontData
	 * @return
	 */
	public static Font get(String fontData) {
		if(!hasFont(fontData)) {
			addFont(fontData);
		}
		
		return fontLib.get(fontData);
	}
	
	private static boolean hasFont(String fontData) {
		return fontLib.containsKey(fontData);
	}
	
	private static void addFont(String fontData) {
		String[] fontParts = fontData.split("_");
		final byte NAME = 0;
		final byte SIZE = 1;
		final byte STYLE = 2;
		Font newFont = new Font(fontParts[NAME], getStyle(fontParts[STYLE]), Integer.parseInt(fontParts[SIZE]));
		fontLib.put(fontData, newFont);
	}
	
	private static int getStyle(String style) {
		style = style.toUpperCase();
		switch(style) {
		case "P":
			return Font.PLAIN;
		case "B":
			return Font.BOLD;
		case "I":
			return Font.ITALIC;
		default:
			return Font.PLAIN;
		}
	}
	
}
