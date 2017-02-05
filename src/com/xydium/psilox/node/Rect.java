package com.xydium.psilox.node;

import static com.xydium.psilox.rendering.Primitives.*;

import com.xydium.psilox.math.Vec;
import com.xydium.psilox.rendering.Color;
import com.xydium.psilox.rendering.Primitives;

public class Rect extends Shape {

	public Rect(String tag, Vec dimensions, boolean centered, Color... colors) {
		super(tag, dimensions, QUADS, centered ? Primitives.C_RECT : Primitives.RECT, colors);
	}
	
}
