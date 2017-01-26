package com.xydium.psilox.rendering;
import java.nio.FloatBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.xydium.psilox.math.Vec3;

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
	
	public static void translate(Vec3 p) {
		gl.glTranslatef(p.x, p.y, p.z);
	}

	public static void rotate(float theta) {
		rotate(theta, Vec3.Z_UNIT);
	}
	
	public static void rotate(float theta, Vec3 a) {
		gl.glRotatef(theta, a.x, a.y, a.z);
	}
	
	public static void scale(Vec3 s) {
		gl.glScalef(s.x, s.y, s.z);
	}
	
	public static void setTransform(Vec3 p, float theta, Vec3 a, Vec3 s) {
		clearTransform();
		translate(p);
		rotate(theta, a);
		scale(s);
	}
	
	public static void setTransform(Vec3 p, float theta, Vec3 s) {
		setTransform(p, theta, Vec3.Z_UNIT, s);
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
	
	public static void fixedFunction(int mode, Vec3[] vertices, Color... colors) {
		gl.glBegin(mode);
		Vec3 idv;
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
