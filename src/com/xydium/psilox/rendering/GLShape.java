package com.xydium.psilox.rendering;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.xydium.psilox.math.Vec3;

public class GLShape {
	
	public final FloatBuffer vertices, colors;
	
	public GLShape(Vec3[] verts, Color... cols) {
		vertices = Buffers.newDirectFloatBuffer(Vec3.toFloatArray(verts));
		colors = Buffers.newDirectFloatBuffer(Color.toFloatArray(cols));
		vertices.rewind();
		colors.rewind();
	}
	
	public GLShape(FloatBuffer verts, Color... cols) {
		vertices = verts;
		colors= Buffers.newDirectFloatBuffer(Color.toFloatArray(cols));
		vertices.rewind();
		colors.rewind();
	}
	
}
