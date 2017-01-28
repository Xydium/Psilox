package com.xydium.psilox.rendering;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.xydium.psilox.math.Vec3;

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
	
	public static final Vec3[] RECT = {
			new Vec3(0, 0),
			new Vec3(1, 0),
			new Vec3(1, 1),
			new Vec3(0, 1)
	};
		
	public static final Vec3[] C_RECT = {
			new Vec3(-.5f, -.5f),
			new Vec3(.5f, -.5f),
			new Vec3(.5f, .5f),
			new Vec3(-.5f, .5f),
	};
	
	public static final Vec3[] UTRI = {
		new Vec3(0, 0),
		new Vec3(1, 0),
		new Vec3(.5f, 1)
	};
	
	public static final Vec3[] EQTRI = {
		new Vec3(0, 0),
		new Vec3(1, 0),
		new Vec3(.5f, .866f)
	};
	
	public static final Vec3[] C_UTRI = {
		new Vec3(0, .5f),
		new Vec3(-.5f, -.5f),
		new Vec3(.5f, -.5f)
	};
	
	public static final Vec3[] C_EQTRI = {
		new Vec3(0, .433f),
		new Vec3(-.5f, -.433f),
		new Vec3(.5f, -.433f)
	};
	
	public static void initPrimitiveBuffers() {
		FB_RECT = Buffers.newDirectFloatBuffer(Vec3.toFloatArray(RECT));
		FB_C_RECT = Buffers.newDirectFloatBuffer(Vec3.toFloatArray(C_RECT));
		FB_UTRI = Buffers.newDirectFloatBuffer(Vec3.toFloatArray(UTRI));
		FB_EQTRI = Buffers.newDirectFloatBuffer(Vec3.toFloatArray(EQTRI));
		FB_C_UTRI = Buffers.newDirectFloatBuffer(Vec3.toFloatArray(C_UTRI));
		FB_C_EQTRI = Buffers.newDirectFloatBuffer(Vec3.toFloatArray(C_EQTRI));
	}
	
}
