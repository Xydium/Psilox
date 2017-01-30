package com.xydium.psilox.rendering;

public class Color {

	public final float r, g, b, a;
	
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Color(float r, float g, float b) {
		this(r, g, b, 1);
	}

	public Color(int r, int g, int b, int a) {
		this(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
	}
	
	public Color(int r, int g, int b) {
		this(r, g, b, 1);
	}
	
	public static float[] toFloatArray(Color[] colors) { 
		float[] res = new float[colors.length * 4];
		int i = 0;
		for(Color v : colors) {
			res[i] = v.r;
			i++;
			res[i] = v.g;
			i++;
			res[i] = v.b;
			i++;
			res[i] = v.a;
			i++;
		}
		return res;
	}
	
}
