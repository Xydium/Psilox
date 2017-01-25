import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GL4;


public class Render {

	private static GL gl;
	private static GL2 gl2;
	private static GL3 gl3;
	private static GL4 gl4;
	
	public static int clearBufferBit;
	
	public static void ready(GL gl) {
		Render.gl = gl;
		Render.gl2 = gl2();
		//Render.gl3 = gl3();
		//Render.gl4 = gl4();
	}
	
	public static GL gl() {
		return gl;
	}

	public static GL2 gl2() {
		return gl.getGL2();
	}
	
	public static GL3 gl3() {
		return gl.getGL3();
	}
	
	public static GL4 gl4() {
		return gl.getGL4();
	}
	
	public static void clear() {
		gl.glClear(clearBufferBit);
	}
	
	public static void clearTransform() {
		gl2.glLoadIdentity();
	}
	
	public static void flush() {
		gl.glFlush();
	}
	
	public static void translate(float x, float y) {
		translate(x, y, 0);
	}
	
	public static void translate(float x, float y, float z) {
		gl2.glTranslatef(x, y, z);
	}

	public static void rotate(float theta) {
		rotate(theta, 0, 0, 1);
	}
	
	public static void rotate(float theta, float axisX, float axisY, float axisZ) {
		gl2.glRotatef(theta, axisX, axisY, axisZ);
	}
	
	public static void combinedBuffers(IntBuffer vertices, FloatBuffer colors, int renderMode) {
		gl2.glVertexPointer(2, GL2.GL_INT, 0, vertices);
		gl2.glColorPointer(3, GL2.GL_FLOAT, 0, colors);
		gl2.glDrawArrays(renderMode, 0, vertices.capacity() / 2);
	}
	
}
