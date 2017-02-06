package com.xydium.psilox.rendering;

import com.jogamp.opengl.GL2;
import com.xydium.psilox.math.Vec;

public class Primitives {

	public static final int QUADS = GL2.GL_QUADS;
	public static final int TRI = GL2.GL_TRIANGLES;
	public static final int LINE = GL2.GL_LINES;
	public static final int POINT = GL2.GL_POINTS;
	public static final int TRI_FAN = GL2.GL_TRIANGLE_FAN;
	public static final int TRI_STRIP = GL2.GL_TRIANGLE_STRIP;
	public static final int LINE_STRIP = GL2.GL_LINE_STRIP;
	
	public static final Vec[] RECT = {
			new Vec(0, 0),
			new Vec(1, 0),
			new Vec(1, 1),
			new Vec(0, 1)
	};
		
	public static final Vec[] C_RECT = {
			new Vec(-.5f, -.5f),
			new Vec(.5f, -.5f),
			new Vec(.5f, .5f),
			new Vec(-.5f, .5f),
	};
	
	public static final Vec[] UTRI = {
		new Vec(0, 0),
		new Vec(1, 0),
		new Vec(.5f, 1)
	};
	
	public static final Vec[] EQTRI = {
		new Vec(0, 0),
		new Vec(1, 0),
		new Vec(.5f, .866f)
	};
	
	public static final Vec[] C_UTRI = {
		new Vec(0, .5f),
		new Vec(-.5f, -.5f),
		new Vec(.5f, -.5f)
	};
	
	public static final Vec[] C_EQTRI = {
		new Vec(0, .433f),
		new Vec(-.5f, -.433f),
		new Vec(.5f, -.433f)
	};
	
}
