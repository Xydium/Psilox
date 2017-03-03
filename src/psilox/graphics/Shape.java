package psilox.graphics;

import org.lwjgl.opengl.GL11;

import psilox.math.Vec;

public class Shape {

	public static final int QUAD = GL11.GL_QUADS;
	public static final int TRI = GL11.GL_TRIANGLES;
	public static final int POLY = GL11.GL_TRIANGLE_FAN;
	public static final int TRI_STRIP = GL11.GL_TRIANGLE_STRIP;
	public static final int LINE = GL11.GL_LINE;
	public static final int LINE_STRIP = GL11.GL_LINE_STRIP;
	
	private float[] verts;
	private float[] colors;
	private byte[] indices;
	private int mode;
	
	public Shape(int mode, Vec[] verts, Color color) {
		this(mode, verts, color, null);
	}
	
	public Shape(int mode, Vec[] verts, Color color, byte[] indices) {
		this(mode, verts, new Color[] {color}, indices);
	}
	
	public Shape(int mode, Vec[] verts, Color[] colors) {
		this(mode, verts, colors, null);
	}
	
	public Shape(int mode, Vec[] verts, Color[] colors, byte[] indices) {
		this.mode = mode;
		this.verts = Vec.toFloatArray(verts);
		this.colors = Color.toFloatArray(colors);
		this.indices = indices;
	}
	
	public void setColor(Color color) {
		colors = new float[] { color.r, color.g, color.b, color.a };
	}

	public void setColors(Color[] colors) {
		this.colors = Color.toFloatArray(colors);
	}
	
	public void setVerts(Vec[] verts) {
		this.verts = Vec.toFloatArray(verts);
	}
	
	public float[] getColors() {
		return colors;
	}
	
	public float[] getVerts() {
		return verts;
	}
	
	public byte[] getIndices() {
		return indices;
	}
	
	public int getMode() {
		return mode;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
	}
	
}
