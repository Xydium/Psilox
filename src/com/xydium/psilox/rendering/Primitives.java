package com.xydium.psilox.rendering;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.xydium.psilox.math.Vec;

public class Primitives {

	public static final int QUADS = GL2.GL_QUADS;
	public static final int TRI = GL2.GL_TRIANGLES;
	public static final int LINE = GL2.GL_LINES;
	public static final int POINT = GL2.GL_POINTS;
	
	public static FloatBuffer FB_RECT;
	public static FloatBuffer FB_C_RECT;
	public static FloatBuffer FB_UTRI;
	public static FloatBuffer FB_EQTRI;
	public static FloatBuffer FB_C_UTRI;
	public static FloatBuffer FB_C_EQTRI;
	
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
	
	public static void initPrimitiveBuffers() {
		FB_RECT = Buffers.newDirectFloatBuffer(Vec.toFloatArray(RECT));
		FB_C_RECT = Buffers.newDirectFloatBuffer(Vec.toFloatArray(C_RECT));
		FB_UTRI = Buffers.newDirectFloatBuffer(Vec.toFloatArray(UTRI));
		FB_EQTRI = Buffers.newDirectFloatBuffer(Vec.toFloatArray(EQTRI));
		FB_C_UTRI = Buffers.newDirectFloatBuffer(Vec.toFloatArray(C_UTRI));
		FB_C_EQTRI = Buffers.newDirectFloatBuffer(Vec.toFloatArray(C_EQTRI));
	}
	
}
