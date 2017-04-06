package psilox.graphics;

import org.lwjgl.opengl.GL11;

import psilox.math.Vec;

public class Shape {

	public static final int QUAD = GL11.GL_QUADS;
	public static final int TRI = GL11.GL_TRIANGLES;
	public static final int POLY = GL11.GL_TRIANGLE_FAN;
	public static final int OUTLINE = GL11.GL_LINE_LOOP;
	public static final int TRI_STRIP = GL11.GL_TRIANGLE_STRIP;
	public static final int LINE = GL11.GL_LINE;
	public static final int LINE_STRIP = GL11.GL_LINE_STRIP;
	
	private float[] verts;
	private float[] colors;
	private byte[] indices;
	private int mode;
	
	/**
	 * Constructs a new shape with a single
	 * color and no indexing.
	 * 
	 * @param mode
	 * @param verts
	 * @param color
	 */
	public Shape(int mode, Vec[] verts, Color color) {
		this(mode, verts, color, null);
	}
	
	/**
	 * Constructs a new shape with a single color
	 * and indexing.
	 * 
	 * @param mode
	 * @param verts
	 * @param color
	 * @param indices
	 */
	public Shape(int mode, Vec[] verts, Color color, byte[] indices) {
		this(mode, verts, new Color[] {color}, indices);
	}
	
	/**
	 * Constructs a shape with multiple colors and no indexing.
	 * 
	 * @param mode
	 * @param verts
	 * @param colors
	 */
	public Shape(int mode, Vec[] verts, Color[] colors) {
		this(mode, verts, colors, null);
	}
	
	/**
	 * Constructs a shape rendered in immediate-mode with
	 * specified render mode, vertices, colors, and indices.
	 * Indices may be null if indexing is not used.
	 * 
	 * @param mode
	 * @param verts
	 * @param colors
	 * @param indices
	 */
	public Shape(int mode, Vec[] verts, Color[] colors, byte[] indices) {
		this.mode = mode;
		this.verts = Vec.toFloatArray(verts);
		this.colors = Color.toFloatArray(colors);
		this.indices = indices;
	}
	
	/**
	 * Sets the shape to use a single color.
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		colors = new float[] { color.r, color.g, color.b, color.a };
	}

	/**
	 * Sets the shape to use multiple colors.
	 * 
	 * @param colors
	 */
	public void setColors(Color[] colors) {
		this.colors = Color.toFloatArray(colors);
	}
	
	/**
	 * Sets the verts of the shape.
	 * 
	 * @param verts
	 */
	public void setVerts(Vec[] verts) {
		this.verts = Vec.toFloatArray(verts);
	}
	
	/**
	 * Returns the colors of the shape in an array
	 * of float values.
	 * 
	 * @return
	 */
	public float[] getColors() {
		return colors;
	}
	
	/**
	 * Returns the verts of the shape in an array
	 * of float values.
	 * 
	 * @return
	 */
	public float[] getVerts() {
		return verts;
	}
	
	/**
	 * Returns the indices of the shape.
	 * 
	 * @return
	 */
	public byte[] getIndices() {
		return indices;
	}
	
	/**
	 * Sets the color for a given vert.
	 * 
	 * @param index
	 * @param color
	 */
	public void setColorAt(int index, Color color) {
		if(index * 4 + 4 <= colors.length) {
			colors[index * 4] = color.r;
			colors[index * 4 + 1] = color.g;
			colors[index * 4 + 2] = color.b;
			colors[index * 4 + 3] = color.a;
		}
	}
	
	/**
	 * Sets a given vert.
	 * 
	 * @param index
	 * @param vert
	 */
	public void setVertAt(int index, Vec vert) {
		if(index * 3 + 3 <= verts.length) {
			verts[index * 3] = vert.x;
			verts[index * 3 + 1] = vert.y;
			verts[index * 3 + 2] = vert.z;
		}
	}
	
	/**
	 * Returns the immediate render mode of the shape.
	 * @return
	 */
	public int getMode() {
		return mode;
	}
	
	/**
	 * Sets the immediate render mode of the shape.
	 * 
	 * @param mode
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public static Shape quadf(Vec offset, Vec dimensions, Color...colors) {
		assert(colors.length == 1 || colors.length == 4);
		Vec[] verts = new Vec[4];
		verts[0] = new Vec(offset);
		verts[1] = offset.sum(dimensions.yc());
		verts[2] = offset.sum(dimensions);
		verts[3] = offset.sum(dimensions.xc());
		return new Shape(QUAD, verts, colors);
	}
	
	public static Shape quado(Vec offset, Vec dimensions, Color...colors) {
		Shape s = quadf(offset, dimensions, colors);
		s.setMode(OUTLINE);
		return s;
	}
	
	public static Shape cquadf(Vec offset, Vec dimensions, Color...colors) {
		return quadf(offset.dif(dimensions.half()), dimensions, colors);
	}
	
	public static Shape cquado(Vec offset, Vec dimensions, Color...colors) {
		return quado(offset.dif(dimensions.half()), dimensions, colors);
	}
	
}
