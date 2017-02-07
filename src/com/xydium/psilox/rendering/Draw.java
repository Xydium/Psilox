package com.xydium.psilox.rendering;
import java.nio.FloatBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.xydium.psilox.math.Transform;
import com.xydium.psilox.math.Vec;

public class Draw {

	private GL2 gl;
	
	public int clearBufferBit = GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT;
	
	public void ready(GL gl) {
		this.gl = gl.getGL2();
	}
	
	public GL2 gl() {
		return gl;
	}
	
	public void clear() {
		gl.glClear(clearBufferBit);
	}
	 
	public void use(Shader shader) {
		gl.glUseProgram(shader.getProgram());
	}
	
	public void unbindShader() {
		gl.glUseProgram(0);
	}
	
	public void clearColor(Color c) {
		gl.glClearColor(c.r, c.g, c.b, c.a);
	}
	
	public void clearTransform() {
		gl.glLoadIdentity();
	}
	
	public void flush() {
		gl.glFlush();
	}
	
	public void translate(Vec p) {
		gl.glTranslatef(p.x, p.y, p.z);
	}

	public void rotate(float theta) {
		rotate(theta, Vec.Z_UNIT);
	}
	
	public void rotate(float theta, Vec a) {
		gl.glRotatef(theta, a.x, a.y, a.z);
	}
	
	public void scale(Vec s) {
		gl.glScalef(s.x, s.y, s.z);
	}
	
	public void setTransform(Transform transform) {
		setTransform(transform.positionGlobal(), transform.rotationGlobal(), Vec.Z_UNIT, transform.scaleGlobal());
	}
	
	public void transform(Transform transform) {
		clearTransform();
		if(transform.getParent() != null) {
			translate(transform.getParent().positionGlobal());
			rotate(transform.getParent().rotationGlobal(), Vec.Z_UNIT);
			scale(transform.getParent().scaleGlobal());
		}
		translate(transform.position());
		rotate(transform.rotation());
		scale(transform.scale());
	}
	
	public void setTransform(Vec p, float theta, Vec a, Vec s) {
		clearTransform();
		translate(p);
		rotate(theta, a);
		scale(s);
	}
	
	public void setTransform(float px, float py, float pz, float theta, float ax, float ay, float az, float sx, float sy, float sz) {
		clearTransform();
		gl.glRotatef(theta, ax, ay, az);
		gl.glTranslatef(px, py, pz);
		gl.glScalef(sx, sy, sz);
	}
	
	public void setTransform(Vec p, float theta, Vec s) {
		setTransform(p, theta, Vec.Z_UNIT, s);
	}
	
	public void combinedBuffers(int renderMode, FloatBuffer vertices, FloatBuffer colors) {
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
	
	public void fixedFunction(int mode, Vec[] vertices, Color... colors) {
		gl.glBegin(mode);
		Vec idv;
		Color idc;
		for(int i = 0; i < vertices.length; i++) {
			idv = vertices[i];
			idc = colors[i % colors.length];
			gl.glColor4f(idc.r, idc.g, idc.b, idc.a);
			gl.glVertex3f(idv.x, idv.y, idv.z);
		}
		gl.glEnd();
	}
	
	public void fixedFunction(int mode, Color color, Vec...vertices) {
		gl.glBegin(mode);
		Vec idv;
		gl.glColor4f(color.r, color.g, color.b, color.a);
		for(int i = 0; i < vertices.length; i++) {
			idv = vertices[i];
			gl.glVertex3f(idv.x, idv.y, idv.z);
		}
		gl.glEnd();
	}
	
}
