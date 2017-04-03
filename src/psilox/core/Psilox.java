package psilox.core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import static psilox.graphics.Draw.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import psilox.audio.Audio;
import psilox.graphics.Draw;
import psilox.input.Input;
import psilox.input.KeySequence;
import psilox.math.Mat4;
import psilox.math.Vec;
import psilox.node.Node;
import psilox.utils.Log;
import psilox.utils.Time;

public class Psilox {

	private static Config config;
	
	private static long updateInterval;
	private static long renderInterval;
	
	private static Thread thread;
	private static boolean running;
	private static boolean clearScreen;
	private static long tick;
	public static float deltaTime;
	public static float updateTime;
	public static float renderTime;
	
	private static long window;
	public static final Node root = new Node();
	private static Node mainNode;
	private static KeySequence terminator;
	private static Node nextScene;
	private static Node cameraTarget;
	
	/**
	 * Begins a new Psilox runtime, of which only one may exist
	 * at a time per Java program, using the configuration
	 * provided, and after initialization, setting the first
	 * child node of root to mainNode.
	 * 
	 * @param config
	 * @param mainNode
	 */
	public static void start(Config config, Node mainNode) {
		Psilox.config = config;
		initLog();
		initIntervals();
		config.logConfig();
		if(running()) return;
		root.tag = "root";
		Psilox.mainNode = mainNode;
		initThread();
	}
	
	/**
	 * Causes Psilox to terminate on the next run of its
	 * internal loop.
	 */
	public static void stop() {
		running = false;
	}
	
	/**
	 * Called by the internal loop or end user to run
	 * an update frame.
	 */
	public static void update() {
		glfwPollEvents();
		tick++;
		root.updateChildren();
		Node.applyChanges();
	}
	
	/**
	 * Called by the internal loop or end user to run
	 * a render frame. 
	 */
	public static void render() {
		clearTransform();
		if(clearScreen) {
			clear();
		}
		
		if(cameraTarget != null) {
			Vec pos = cameraTarget.globalPosition().scl(-1).sum(cameraTarget.viewSize().half());
			float rot = cameraTarget.globalRotation();
			Draw.pushTransform(Mat4.transform(pos, rot));
			root.renderChildren();
			Draw.popTransform();
		} else {
			root.renderChildren();
		}
		
		int error = glGetError();
		if(error != GL_NO_ERROR) {
			Log.error("Last GL Error: %d", error);
		}
		
		if(config.doubleBuffer) {
			glfwSwapBuffers(window);
		} else {
			glFlush();
		}
	}
	
	private static void loop() {
		long lastUpdate = Time.now() - updateInterval;
		long lastRender = Time.now() - renderInterval;
		
		initWindow();
		clear();
		root.addChild(mainNode);
		
		while(running()) {
			if(nextScene != null) {
				root.swapChild(nextScene, 0);
				nextScene = null;
			}
			if(updateInterval != Config.MANUAL && Time.since(lastUpdate) >= updateInterval) {
				deltaTime = Time.since(lastUpdate) / (float) Time.SECOND;
				lastUpdate = Time.now();
				update();
				updateTime = (Time.now() - lastUpdate) / (float) Time.SECOND;
			}
			if(renderInterval != Config.MANUAL && Time.since(lastRender) >= renderInterval) {
				lastRender = Time.now();
				render();
				renderTime = (Time.now() - lastRender) / (float) Time.SECOND;
			}
			if(glfwWindowShouldClose(window)) {
				running = false;
			}
		}
		
		glfwDestroyWindow(window);
		glfwTerminate();
		
		Input.dumpListeners();
		Audio.shutdown();
	}
	
	/**
	 * Sets the target scene to nextScene, with root's
	 * first child switched accordingly on the next
	 * internal loop cycle.
	 * 
	 * @param nextScene
	 */
	public static void changeScene(Node nextScene) {
		Psilox.nextScene = nextScene;
	}
	
	/**
	 * Sets the node which the camera will translate to center
	 * in the screen regardless of its actual position. All other
	 * geometry will be accordingly shifted.
	 * 
	 * @param cameraTarget
	 */
	public static void cameraTarget(Node cameraTarget) {
		Psilox.cameraTarget = cameraTarget;
	}
	
	private static void initLog() {
		if(config.terminationSequence.length > 0) {
			terminator = new KeySequence(Psilox::stop, config.terminationSequence).asCombination();
		}
		Log.setLogLevel(config.logLevel);
		Log.setConsoleEnabled(config.console);
	}
	
	private static void initIntervals() {
		updateInterval = calculateInterval(config.updateRate);
		renderInterval = calculateInterval(config.frameRate);
	}
	
	private static void initThread() {
		running = true;
		if(System.getProperty("os.name").contains("Mac")) {
			Log.warning("Mac OSX does not support GLFW windows on alternate threads, running Psilox on main thread.");
			Log.warning("Mac OSX does not support OpenGL 2.2+ with Fixed-Function. Using Legacy OpenGL 2.1 instead.");
			Log.warning("Mac OSX GL 2.1 does not support GLSL version 130 or higher, use shader version 120 instead.");
			System.setProperty("java.awt.headless", "true");
			Log.warning("AWT cannot start it's event loop since GLFW is confined to the main thread. Resolving this causes an error you can safely ignore.");
			Log.warning("Drawing to custom framebuffers fails on OpenGL 2.1, do not attempt.");
			loop();
		} else {
			thread = new Thread(() -> { loop(); });
			thread.start();
		}
	}
	
	private static void initWindow() {
		if(!glfwInit()) {
			Log.error("Initializing GLFW failed.");
			return;
		}
		
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		glfwWindowHint(GLFW_DOUBLEBUFFER, config.doubleBuffer ? GL_TRUE : GL_FALSE);
		glfwWindowHint(GLFW_DECORATED, config.undecorated ? GL_FALSE : GL_TRUE);
		window = glfwCreateWindow(config.width, config.height, config.title, NULL, NULL);
		if(window == NULL) {
			Log.error("Creating GLFW window failed.");
			return;
		}
		
		GLFWVidMode m = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (m.width() - config.width) / 2, (m.height() - config.height) / 2);
		
		if(config.fullscreen) {
			glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, m.width(), m.height(), 60);
			Input.scale = new Vec(config.width / (float) m.width(), config.height / (float) m.height());
		}
		
		glfwSetKeyCallback(window, Input.keyCallback);
		glfwSetMouseButtonCallback(window, Input.mouseCallback);
		glfwSetCursorPosCallback(window, Input.cursorCallback);
		glfwSetCharCallback(window, Input.charCallback);
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GL.createCapabilities();
		
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glClearColor(config.clearColor.r, config.clearColor.g, config.clearColor.b, config.clearColor.a);
		clearScreen = config.clearscreen;
		Draw.immediateMode = config.immediateMode;
		
		if(config.fullscreen) {
			glViewport(0, 0, m.width(), m.height());
		} else {
			glViewport(0, 0, config.width, config.height);
		}
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, config.width, 0, config.height, -10, 10);
		Draw.projection = Mat4.orthographic(0, config.width, 0, config.height, -10, 10);
		glMatrixMode(GL_MODELVIEW);
		Input.WINDOW_HEIGHT = config.height;
		Audio.init();
	}
	
	private static long calculateInterval(int ps) {
		return ps <= 0 ? ps : (long) (Time.SECOND / ps);
	}
	
	/**
	 * Returns the number of ticks elapsed since the
	 * engine started.
	 * 
	 * @return
	 */
	public static long ticks() {
		return tick;
	}
	
	/**
	 * Returns whether the engine is still running.
	 * 
	 * @return
	 */
	public static boolean running() {
		return running;
	}
	
	/**
	 * Returns the config for the engine. If values inside
	 * are depended on, ensure that the config remains unaltered,
	 * as changes will not apply to the current runtime.
	 * 
	 * @return
	 */
	public static Config config() {
		return config;
	}
	
}
