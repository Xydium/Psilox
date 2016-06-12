package com.xydium.resources;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class ColorLib {

	private static Map<String, Color> colorMap = new HashMap<String, Color>();
	
	public static Color get(String colorData) {
		if(!hasColor(colorData)) {
			addColor(colorData);
		}
		
		return colorMap.get(colorData);
	}
	
	private static boolean hasColor(String colorData) {
		return colorMap.containsKey(colorData);
	}
	
	private static void addColor(String colorData) {
		short[] values = splitData(colorData);
		Color c = new Color(values[1], values[2], values[3], values[0]);
		colorMap.put(colorData, c);
	}
	
	private static short[] splitData(String colorData) {
		if(colorData.length() == 6) { 
			colorData = "FF" + colorData.toUpperCase();
		}
		
		short[] values = new short[4];
		for(int i = 0; i < 4; i++) {
			values[i] = Short.parseShort(colorData.substring(i*2, i*2+2), 16);
		}
		
		return values;
	}
	
}
