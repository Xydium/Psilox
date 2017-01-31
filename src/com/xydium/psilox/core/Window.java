package com.xydium.psilox.core;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

public class Window extends JFrame implements GLEventListener {

	private static final long serialVersionUID = 1L;
	
	private int width;
	private int height;
	private boolean fullscreen;
	private Psilox psilox;
	
	private GLU glu;
	//private GLUT glut;
	private GLCapabilities caps;
	private GLCanvas canvas;
	
	public Window(String title, int width, int height, boolean fullscreen, final Psilox psilox) {
		super(title);
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
		this.psilox = psilox;
		GLProfile prof = GLProfile.get(GLProfile.GL2);
		caps = new GLCapabilities(prof);
		caps.setDoubleBuffered(psilox.config.doubleBuffer);
		canvas = new GLCanvas(caps);
		canvas.addGLEventListener(this);
		getContentPane().add(canvas);
	}
	
	public void setup() {
		canvas.setSize(width, height);
		canvas.addKeyListener(psilox.input());
		canvas.addMouseListener(psilox.input());
		canvas.addMouseMotionListener(psilox.input());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) { psilox.stop(); }
		});
		setResizable(false);
		if(fullscreen) {
			setExtendedState(MAXIMIZED_BOTH);
			setUndecorated(true);
		} else {
			setLocationRelativeTo(null);
		}
		pack();
		setVisible(true);
		canvas.requestFocusInWindow();
	}
	
	public void render() {
		canvas.display();
	}
	
	public void display(GLAutoDrawable d) {
		psilox.draw().ready(d.getGL());
		psilox.render();
		psilox.draw().flush();
	}

	public void dispose(GLAutoDrawable d) {
		
	}

	public void init(GLAutoDrawable d) {
		GL gl = d.getGL();
		glu = new GLU();
		//glut = new GLUT();
		gl.getGL2().glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.getGL2().glEnableClientState(GL2.GL_COLOR_ARRAY);
		psilox.draw().ready(d.getGL().getGL2());
		psilox.draw().clearColor(psilox.config.clearColor);
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

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
