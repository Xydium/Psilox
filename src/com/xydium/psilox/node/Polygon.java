package com.xydium.psilox.node;

import com.jogamp.opengl.GL;
import com.xydium.psilox.math.Vec;
import com.xydium.psilox.rendering.Color;

public class Polygon extends Shape {

	public Polygon(String tag, Vec[] vertices, Color... colors) {
		super(tag, Vec.ONE, GL.GL_TRIANGLE_FAN, vertices, colors);
	}
	
}
