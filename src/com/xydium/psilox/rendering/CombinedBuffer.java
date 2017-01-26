package com.xydium.psilox.rendering;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;

public class CombinedBuffer {
	
	private FloatBuffer vertices, colors;
	
	public CombinedBuffer(Vertex[] verts, Color color) {
		this(verts, new Color[]{color});
	}
	
	public CombinedBuffer(Vertex[] verts, Color[] cols) {
		vertices = Buffers.newDirectFloatBuffer(Vertex.toFloatArray(verts));
		colors = Buffers.newDirectFloatBuffer(Color.toFloatArray(cols));
		vertices.rewind();
		colors.rewind();
	}
	
	public void render(int mode) {
		Render.combinedBuffers(mode, vertices, colors);
	}
	
}
