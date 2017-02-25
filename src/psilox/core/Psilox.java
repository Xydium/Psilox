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
	
	public static void start(Config config, Node mainNode) {
		Psilox.config = config;
		initLog();
		initIntervals();
		config.logConfig();
		if(running()) return;
		root.tag = "Root";
		Psilox.mainNode = mainNode;
		initThread();
	}
	
	public static void stop() {
		running = false;
	}
	
	public static void update() {
		glfwPollEvents();
		tick++;
		root.updateChildren();
		Node.applyChanges();
	}
	
	public static void render() {
		clearTransform();
		if(clearScreen) {
			clear();
		}
		
		root.renderChildren();
		
		int error = glGetError();
		if(error != GL_NO_ERROR) {
			Log.error("Last GL Error: %d", error);
		}
		
		glfwSwapBuffers(window);
	}
	
	private static void loop() {
		long lastUpdate = Time.now() - updateInterval;
		long lastRender = Time.now() - renderInterval;
		
		initWindow();
		root.addChild(mainNode);
		
		while(running()) {
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
			Log.warning("Do yourself a favor: Stop spending $3000 on outdated hardware with obnoxiously restrictive software.");
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
	
	public static long ticks() {
		return tick;
	}
	
	public static float deltaTime() {
		return deltaTime;
	}
	
	public static boolean running() {
		return running;
	}
	
	public static Config config() {
		return config;
	}
	
}
