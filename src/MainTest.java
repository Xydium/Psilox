import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.swing.JFrame;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

public class MainTest extends JFrame implements GLEventListener {

	private static final int width = 800;
	private static final int height = 600;
	
	private int program;
	
	private IntBuffer buffers = IntBuffer.allocate(2);
	private IntBuffer vertexArray = IntBuffer.allocate(1);
	private float[] square = {
			-.5f, -.5f,
			.5f, -.5f,
			.5f, .5f,
			.5f, .5f,
			-.5f, .5f,
			-.5f, -.5f
	};
	
	private float[] colorData = {
		1, 0, 0,
		1, 1, 0,
		0, 1, 0,
		0, 1, 0,
		0, 0, 1,
		1, 0, 0
	};
	
	FloatBuffer vertexFB = FloatBuffer.wrap(square);
	FloatBuffer colorFB = FloatBuffer.wrap(colorData);
	
	public MainTest() {
		super("Test");
		
		GLProfile profile = GLProfile.get(GLProfile.GL4);
		GLCapabilities capabilities = new GLCapabilities(profile);
		
		GLCanvas canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		
		setName("Test");
		getContentPane().add(canvas);
		
		setSize(width, height);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		
		canvas.requestFocusInWindow();
	}
	
	public void display(GLAutoDrawable d) {
		GL4 gl = d.getGL().getGL4();
		gl.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
		
		gl.glUseProgram(program);
		gl.glBindVertexArray(vertexArray.get(0));
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, 6);
		
		//gl.glFlush();
	}

	public void dispose(GLAutoDrawable d) {
	
	}

	public void init(GLAutoDrawable d) {
		GL4 gl = d.getGL().getGL4();
		
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		
		gl.glEnable(GL4.GL_DEPTH_TEST);
		gl.glClearDepthf(10.0f);
		gl.glClearColor(0.392f, 0.584f, 0.929f, 1.0f);
		gl.glDepthFunc(GL4.GL_LEQUAL);
		
		gl.glGenBuffers(2, buffers);
		
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers.get(0));
		gl.glBufferData(GL4.GL_ARRAY_BUFFER, 4 * 6 * 2, vertexFB, GL4.GL_STATIC_DRAW);
		

		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers.get(1));
		gl.glBufferData(GL4.GL_ARRAY_BUFFER, 4 * 6 * 3, colorFB, GL4.GL_STREAM_DRAW);
		
		gl.glGenVertexArrays(1, vertexArray);
		gl.glBindVertexArray(vertexArray.get(0));
		
		gl.glEnableVertexAttribArray(0);
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers.get(0));
		gl.glVertexAttribPointer(0, 2, GL4.GL_FLOAT, false, 0, 0);
		
		gl.glEnableVertexAttribArray(1);
		gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, buffers.get(1));
		gl.glVertexAttribPointer(1, 3, GL4.GL_FLOAT, false, 0, 0);
		
		program = gl.glCreateProgram();
		
		int vertexShader = gl.glCreateShader(GL4.GL_VERTEX_SHADER);
		
		String[] vertexShaderSource = new String[1];
		vertexShaderSource[0] = "#version 330\n" +
		    "layout(location=0) in vec2 position;\n" +
		    "layout(location=1) in vec3 color;\n" +
		    "out vec3 vColor;\n" +
		    "void main(void)\n" +
		    "{\n" +
		    "gl_Position = vec4(position, 0.0, 1.0);\n" +
		    "vColor = vec4(color, 1.0);\n" +
		    "}\n";
		gl.glShaderSource(vertexShader, 1, vertexShaderSource, null);
		gl.glCompileShader(vertexShader);
		
		int fragmentShader = gl.glCreateShader(GL4.GL_FRAGMENT_SHADER);
		String[] fragmentShaderSource = new String[1];
		fragmentShaderSource[0] = "#version 330\n" +
		    "in vec4 vColor;\n" +
		    "void main(void)\n" +
		    "{\n" +
		    "gl_FragColor = vColor;\n" +
		    "}\n";
		gl.glShaderSource(fragmentShader, 1, fragmentShaderSource, null);
		gl.glCompileShader(fragmentShader);

		gl.glAttachShader(program, vertexShader);
		gl.glAttachShader(program, fragmentShader);
		gl.glLinkProgram(program);
	}

	public void reshape(GLAutoDrawable d, int x, int y, int w, int h) {
		GL4 gl = d.getGL().getGL4();
	}

	public static void main(String[] args) {
		MainTest test = new MainTest();
	}
	
}
