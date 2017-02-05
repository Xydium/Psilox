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

/**
 * Window is a JFrame-extending implementer of GLEventListener, and is created
 * by the Psilox runtime, configured through PsiloxConfig. Window defaults to
 * using GLProfile GL2, which includes features up through GL 3.0. For more
 * advanced functionality, functions within Window must be overridden, and the
 * Psilox class appropriately extended and overridden to create windows with
 * the new class. 
 * 
 * @author Xydium
 *
 */
public class Window extends JFrame implements GLEventListener {

	protected final PsiloxConfig config;
	
	protected final int width, height;
	protected final boolean fullscreen;
	protected final Psilox psilox;
	
	protected GLU glu;
	protected GLCapabilities caps;
	protected GLCanvas canvas;
	
	/**
	 * Constructs this JFrame using the provided PsiloxConfig and Runtime,
	 * setting internal variables and adjusting the config in the event of
	 * fullscreen being enabled. Sets the content pane to a GLCanvas, resizes,
	 * and attaches the necessary listeners, before displaying the window.
	 * 
	 * @param config
	 * @param psilox
	 */
	Window(PsiloxConfig config, Psilox psilox) {
		super(config.title);
		this.config = config;
		this.fullscreen = config.fullscreen;
		this.width = fullscreen? config.width = (int)config.monitorSize().x : config.width;
		this.height = fullscreen? config.height = (int)config.monitorSize().y : config.height;
		this.psilox = psilox;
		
		getContentPane().add(canvas = makeCanvas());
		
		adjustSizes();
		attachInputAndListeners();
	
		setVisible(true);
		canvas.requestFocusInWindow();
	}
	
	/**
	 * Called by Psilox to tell the GLCanvas to redraw.
	 */
	public void render() {
		canvas.display();
	}
	
	/**
	 * Called by the GLCanvas to initiate redraw, attaching the
	 * GL context to Draw, rendering the node tree, and then
	 * flushing the rendering.
	 */
	public void display(GLAutoDrawable d) {
		psilox.draw().ready(d.getGL());
		psilox.render();
		psilox.draw().flush();
	}
			
	/**
	 * Called by the GLCanvas to run setup commands in GL. This
	 * function configures blending and enables vertex arrays, in addition
	 * to loading the clearcolor to Draw.
	 */
	public void init(GLAutoDrawable d) {
		GL gl = d.getGL();
		glu = new GLU();
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		gl.getGL2().glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.getGL2().glEnableClientState(GL2.GL_COLOR_ARRAY);
		psilox.draw().ready(d.getGL().getGL2());
		psilox.draw().clearColor(config.clearColor);
	}

	/**
	 * Called by the GLCanvas to reshape the window. Used to configure
	 * the Orthographic viewport. 
	 */
	public void reshape(GLAutoDrawable d, int x, int y, int w, int h) {
		GL2 gl = d.getGL().getGL2();
		gl.glViewport(0, 0, w, h);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluOrtho2D(0.0, w, 0.0, h);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	/**
	 * Called by the GLCanvas to dispose of resources. Currently unused.
	 */
	public void dispose(GLAutoDrawable d) {
		
	}
	
	/**
	 * Makes the canvas using GL2 profile with either single or double buffering.
	 * 
	 * @return the completed GLCanvas
	 */
	protected GLCanvas makeCanvas() {
		GLProfile prof = GLProfile.get(GLProfile.GL2);
		caps = new GLCapabilities(prof);
		caps.setDoubleBuffered(config.doubleBuffer);
		GLCanvas canvas = new GLCanvas(caps);
		canvas.addGLEventListener(this);
		return canvas;
	}
	
	/**
	 * Sizes the existing canvas after a call to makeCanvas, and
	 * does the necessary fullscreen adjustments, before packing.
	 */
	private void adjustSizes() {
		canvas.setSize(width, height);
		setResizable(false);
		setUndecorated(psilox.config().fullscreen | psilox.config().undecorated);
		if(fullscreen) {
			setExtendedState(MAXIMIZED_BOTH);
		} else {
			setLocationRelativeTo(null);
		}
		pack();
	}
	
	/**
	 * Attaches the Psilox input() object as a listener and
	 * ensures that exiting the JFrame calls psilox.stop() instead.
	 */
	private void attachInputAndListeners() {
		canvas.addKeyListener(psilox.input());
		canvas.addMouseListener(psilox.input());
		canvas.addMouseMotionListener(psilox.input());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) { psilox.stop(); }
		});
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
}
