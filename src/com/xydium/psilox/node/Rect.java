package com.xydium.psilox.node;

import static com.xydium.psilox.rendering.Primitives.*;

import com.xydium.psilox.math.Vec;
import com.xydium.psilox.rendering.Color;
import com.xydium.psilox.rendering.GLShape;

public class Rect extends Shape {

	public Rect(String tag, Vec dimensions, boolean centered, Color... colors) {
		super(tag, dimensions, QUADS, new GLShape(centered ? FB_C_RECT : FB_RECT, colors));
	}
	
}
