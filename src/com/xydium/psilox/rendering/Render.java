package com.xydium.psilox.rendering;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

public class Render {

	private static GL2 gl;
	
	public static int clearBufferBit;
	
	public static void ready(GL gl) {
		Render.gl = gl.getGL2();
	}
	
	public static void clear() {
		gl.glClear(clearBufferBit);
	}
	
	public static void clearColor(Color c) {
		gl.glClearColor(c.r, c.g, c.b, c.a);
	}
	
	public static void clearTransform() {
		gl.glLoadIdentity();
	}
	
	public static void flush() {
		gl.glFlush();
	}
	
	public static void translate(float x, float y) {
		translate(x, y, 0);
	}
	
	public static void translate(float x, float y, float z) {
		gl.glTranslatef(x, y, z);
	}

	public static void rotate(float theta) {
		rotate(theta, 0, 0, 1);
	}
	
	public static void rotate(float theta, float axisX, float axisY, float axisZ) {
		gl.glRotatef(theta, axisX, axisY, axisZ);
	}
	
	public static void scale(float x, float y) {
		scale(x, y, 1);
	}
	
	public static void scale(float x, float y, float z) {
		gl.glScalef(x, y, z);
	}
	
	public static void setTransform(float x, float y, float z, float theta, float axisX, float axisY, float axisZ, float sx, float sy, float sz) {
		clearTransform();
		gl.glTranslatef(x, y, z);
		gl.glRotatef(theta, axisX, axisY, axisZ);
		gl.glScalef(sx, sy, sz);
	}
	
	public static void setTransform(float x, float y, float theta) {
		setTransform(x, y, 0, theta);
	}
	
	public static void setTransform(float x, float y, float z, float theta) {
		setTransform(x, y, z, theta, 1, 1);
	}
	
	public static void setTransform(float x, float y, float z, float theta, float sx, float sy) {
		setTransform(x, y, z, theta, 0, 0, 1, sx, sy, 1);
	}
	
	public static void combinedBuffers(int renderMode, FloatBuffer vertices, FloatBuffer colors) {
		gl.glVertexPointer(3, GL2.GL_FLOAT, 0, vertices);
		if(colors.capacity() == 4) {
			gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
			gl.glColor4f(colors.get(0), colors.get(1), colors.get(2), colors.get(3));
		} else {
			gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
			gl.glColorPointer(4, GL2.GL_FLOAT, 0, colors);
		}
		gl.glDrawArrays(renderMode, 0, vertices.capacity() / 3);
	}
	
	public static void fixedFunction(int mode, Vertex[] vertices, Color... colors) {
		gl.glBegin(mode);
		Vertex idv;
		Color idc;
		for(int i = 0; i < vertices.length; i++) {
			idv = vertices[i];
			idc = colors[i % colors.length];
			gl.glColor4f(idc.r, idc.g, idc.b, idc.a);
			gl.glVertex3f(idv.x, idv.y, idv.z);
		}
		gl.glEnd();
	}
	
}
