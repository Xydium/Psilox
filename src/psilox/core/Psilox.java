package psilox.core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;
import static psilox.graphics.Draw.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.input.Input;
import psilox.math.Vec;
import psilox.utils.Log;
import psilox.utils.Time;

public class Psilox {

	private Config config;
	
	private long updateInterval;
	private long renderInterval;
	
	private Thread thread;
	private boolean running;
	private boolean clearScreen;
	private long tick;
	private float deltaTime;
	
	private long window;
	
	public Psilox(Config config) {
		this.config = config;
		initLog();
		initIntervals();
		config.logConfig(this);
	}
	
	public void start() {
		if(running()) return;
		initThread();
	}
	
	public void stop() {
		running = true;
	}
	
	public void update() {
		glfwPollEvents();
		tick++;
	}
	
	public void render() {
		clearTransform();
		if(clearScreen) {
			clear();
		}
		
		translate(new Vec(250));
		glLineWidth(2);
		arc(Color.LIGHT_GRAY, Vec.ZERO, 20, tick, 360 - (tick % 360), 30);
		translate(Vec.Z_UNIT);
		arc(Color.RED, Vec.ZERO, 20, 0, tick, 30);
		
		int error = glGetError();
		if(error != GL_NO_ERROR) {
			Log.error("%d", error);
		}
		
		glfwSwapBuffers(window);
	}
	
	private void loop() {
		long lastUpdate = Time.now() - updateInterval;
		long lastRender = Time.now() - renderInterval;
		
		initWindow();
		
		while(running()) {
			if(updateInterval != Config.MANUAL && Time.since(lastUpdate) >= updateInterval) {
				deltaTime = Time.since(lastUpdate) / (float) Time.SECOND;
				lastUpdate = Time.now();
				update();
			}
			if(renderInterval != Config.MANUAL && Time.since(lastRender) >= renderInterval) {
				lastRender = Time.now();
				render();
			}
			if(glfwWindowShouldClose(window)) {
				running = false;
			}
		}
		
		glfwDestroyWindow(window);
		glfwTerminate();
		
		Input.dumpListeners();
	}
	
	private void initLog() {
		Log.setLogLevel(config.logLevel);
		Log.setConsoleEnabled(config.console);
	}
	
	private void initIntervals() {
		updateInterval = calculateInterval(config.ups);
		renderInterval = calculateInterval(config.fps);
	}
	
	private void initThread() {
		running = true;
		thread = new Thread(() -> { loop(); });
		thread.start();
	}
	
	private void initWindow() {
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
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		GL.createCapabilities();
		
		glEnable(GL_DEPTH_TEST);
		glActiveTexture(GL_TEXTURE1);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glClearColor(config.clearColor.r, config.clearColor.g, config.clearColor.b, config.clearColor.a);
		clearScreen = config.clearscreen;
		
		if(config.fullscreen) {
			glViewport(0, 0, m.width(), m.height());
		} else {
			glViewport(0, 0, config.width, config.height);
		}
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, config.width, 0, config.height, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		Input.WINDOW_HEIGHT = config.height;
		Draw.loadDrawShaders();
	}
	
	private long calculateInterval(int ps) {
		return ps <= 0 ? ps : (long) (Time.SECOND / ps);
	}
	
	public long ticks() {
		return tick;
	}
	
	public float deltaTime() {
		return deltaTime;
	}
	
	public boolean running() {
		return running;
	}
	
	public Config config() {
		return config;
	}
	
}
