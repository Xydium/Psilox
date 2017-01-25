import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.swing.JFrame;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;


public class AltTest extends JFrame implements GLEventListener{

	private GLU glu;
	private GLUT glut;
	private GLCapabilities caps;
	private GLCanvas canvas;
	private int program;
	
	private static IntBuffer verticesBuf;
	private static FloatBuffer colorsBuf;
	private static IntBuffer verticesBuf1;
	private static FloatBuffer colorsBuf1;
	
	private float angle;
	
	public AltTest() {
		super("AltTest");
		
		GLProfile prof = GLProfile.get(GLProfile.GL2);
		caps = new GLCapabilities(prof);
		canvas = new GLCanvas(caps);
		canvas.addGLEventListener(this);
		getContentPane().add(canvas);
	}
	
	public void run() {
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		canvas.requestFocusInWindow();
		while(true) {
			canvas.display();
			angle += 4;
			angle %= 360;
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new AltTest().run();
	}
	
	public void init(GLAutoDrawable d) {
		GL gl = d.getGL();
		glu = new GLU();
		glut = new GLUT();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		System.out.println(gl.glGetString(GL.GL_VERSION));
		setupPointers(gl.getGL2());
	}
	
	public void display(GLAutoDrawable d) {
		Render.ready(d.getGL());
		Render.clearBufferBit = GL.GL_COLOR_BUFFER_BIT;
		Render.clear();
		
		Render.clearTransform();
		Render.translate(200, 0);
		Render.rotate(angle);
		
		Render.combinedBuffers(verticesBuf1, colorsBuf1, GL.GL_TRIANGLES);
		
		/*
		gl.glLoadIdentity();
		gl.glTranslatef(200.0f, 100.0f, 0.0f);
		gl.glRotatef(angle, 0, 0, 1);
		
		gl.glVertexPointer(2, GL2.GL_INT, 0, verticesBuf);
		gl.glColorPointer(3, GL2.GL_FLOAT, 0, colorsBuf);
		gl.glDrawArrays(GL2.GL_QUADS, 0, 4);
		
		gl.glLoadIdentity();
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex2f(10, 10);
		gl.glVertex2f(100, 100);
		gl.glVertex2f(200, 10);
		gl.glVertex2f(100, 10);
		gl.glVertex2f(300, 10);
		gl.glVertex2f(100, 100);
		gl.glVertex2f(400, 10);
		gl.glVertex2f(100, 100);
		gl.glEnd();
		
		*/
		
		Render.flush();
	}
	
	public void reshape(GLAutoDrawable d, int x, int y, int w, int h) {
		GL2 gl = d.getGL().getGL2();
		gl.glViewport(0, 0, w, h);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(0.0, w, 0.0, h);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void setupPointers(GL2 gl) {
		int vertices[] = new int[] {-50, -50, 50, -50, 50, 50, -50, 50};
		float colors[] = new float[] {1.0f, 0.2f, 0.2f, 0.2f, 0.2f, 1.0f, 0.8f, 1.0f, 0.2f, 0.75f, 0.75f, 0.75f};
		IntBuffer tmpVerticesBuf = Buffers.newDirectIntBuffer(vertices);
		FloatBuffer tmpColorsBuf = Buffers.newDirectFloatBuffer(colors);
		tmpVerticesBuf.rewind();
		tmpColorsBuf.rewind();
		
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
		AltTest.verticesBuf = tmpVerticesBuf;
		AltTest.colorsBuf = tmpColorsBuf;
		AltTest.verticesBuf1 = Buffers.newDirectIntBuffer(new int[]{10, 10, 50, 50, 100, 10});
		AltTest.colorsBuf1 = Buffers.newDirectFloatBuffer(new float[]{1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f});
	}
	
	public void dispose(GLAutoDrawable arg0) {
		
	}
	
}
