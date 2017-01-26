package com.xydium.psilox.rendering;

public class Vertex {

	public final float x, y, z;
	
	public Vertex(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vertex(float x, float y) {
		this(x, y, 0);
	}
	
	public static float[] toFloatArray(Vertex[] vertices) { 
		float[] res = new float[vertices.length * 3];
		int i = 0;
		for(Vertex v : vertices) {
			res[i] = v.x;
			i++;
			res[i] = v.y;
			i++;
			res[i] = v.z;
			i++;
		}
		return res;
	}
	
}
