package com.xydium.psilox.rendering;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.graph.geom.Vertex;
import com.xydium.psilox.math.Vec3;

public class CombinedBuffer {
	
	private FloatBuffer vertices, colors;
	
	public CombinedBuffer(Vec3[] verts, Color color) {
		this(verts, new Color[]{color});
	}
	
	public CombinedBuffer(Vec3[] verts, Color[] cols) {
		vertices = Buffers.newDirectFloatBuffer(Vec3.toFloatArray(verts));
		colors = Buffers.newDirectFloatBuffer(Color.toFloatArray(cols));
		vertices.rewind();
		colors.rewind();
	}
	
	public void render(int mode) {
		Render.combinedBuffers(mode, vertices, colors);
	}
	
}
