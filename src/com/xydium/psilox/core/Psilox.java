package com.xydium.psilox.core;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.xydium.psilox.input.Input;
import com.xydium.psilox.math.Vec;
import com.xydium.psilox.node.Node;
import com.xydium.psilox.rendering.Draw;
import com.xydium.psilox.rendering.Primitives;
import com.xydium.psilox.utilities.Audio;
import com.xydium.psilox.utilities.Log;
import com.xydium.psilox.utilities.Time;

public class Psilox {

	private static boolean exclusiveRuntime = true;

	private int id;
	
	private PsiloxConfig config;
	
	private long updateInterval;
	private long renderInterval;
	
	private Thread thread;
	private boolean running;
	private boolean clearScreen;
	private long tick;
	private float deltaTime;
	
	private Window window;
	private Draw draw;
	private Input input;
	private Audio audio;
	private NodeTree tree;
	private Terminator terminator;
	
	private Psilox(PsiloxConfig config, int id) {
		this.config = config;
		this.id = id;
		initLog();
		initIntervals();
		initWindow();
		Primitives.initPrimitiveBuffers();
		config.logConfig(this);
	}
	
	public void reconfigure(PsiloxConfig config) {
		this.config = config;
		initLog();
		initIntervals();
		window.dispose();
		if(config.fullscreen) {
			Vec w = config.monitorSize();
			config.width = (int) w.x;
			config.height = (int) w.y;
		}
		window = new Window(config.title, config.width, config.height, config.fullscreen, this);
		window.setup();
	}
	
	public void start(Node mainNode) {
		if(running()) return;
		initThread();
		tree.getRoot().addChild(mainNode);
	}
	
	public void stop() {
		setRunning(false);
	}
	
	public void update() {
		tree.update();
		tick++;
	}
	
	public void render() {
		if(clearScreen) draw.clear();
		tree.render();
	}
	
	public long ticks() {
		return tick;
	}
	
	public float deltaTime() {
		return deltaTime;
	}

	public int id() {
		return id;
	}
	
	public boolean running() {
		return running;
	}
	
	public PsiloxConfig config() {
		return config;
	}
	
	public Window window() {
		return window;
	}
	
	public Draw draw() {
		return draw;
	}
	
	public Input input() {
		return input;
	}
	
	public Audio audio() {
		return audio;
	}
	
	private void loop() {
		long lastUpdate = Time.now() - updateInterval;
		long lastRender = Time.now() - renderInterval;
		long lastTerminate = Time.now() - Time.SECOND / 10;
		
		while(running()) {
			if(updateInterval != PsiloxConfig.MANUAL && Time.since(lastUpdate) >= updateInterval) {
				deltaTime = Time.since(lastUpdate) / (float) Time.SECOND;
				lastUpdate = Time.now();
				update();
			}
			if(renderInterval != PsiloxConfig.MANUAL && Time.since(lastRender) >= renderInterval) {
				lastRender = Time.now();
				window.render();
			}
			if(Time.since(lastTerminate) >= Time.SECOND / 10) {
				lastTerminate = Time.now();
				if(terminator.shouldTerminate(input())) {
					Psilox.stopAll();
				}
			}
		}
		
		exit();
	}
	
	private void exit() {
		input.dumpListeners();
		audio.destroy();
		window.dispose();
		runtimeRegistry.remove(this);
		if(runtimeRegistry.size() == 0) {
			System.exit(0);
		} else {
			try {
				thread.join();
			} catch (InterruptedException e) {
				Log.error(e);
			}
		}
	}
	
	private void setRunning(boolean running) {
		this.running = running;
	}
	
	private void initLog() {
		Log.setLogLevel(config.logLevel);
		Log.setConsoleEnabled(config.console);
	}
	
	private void initIntervals() {
		updateInterval = calculateInterval(config.ups);
		renderInterval = calculateInterval(config.fps);
	}
	
	private void initWindow() {
		clearScreen = config.clearscreen;
		input = new Input();
		draw = new Draw();
		audio = new Audio();
		terminator = new Terminator(config.terminationSequence);
		tree = new NodeTree(this);
		if(config.fullscreen) {
			Vec w = config.monitorSize();
			config.width = (int) w.x;
			config.height = (int) w.y;
		}
		window = new Window(config.title, config.width, config.height, config.fullscreen, this);
		window.setup();
		draw.clearBufferBit = GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT;
	}
	
	private void initThread() {
		setRunning(true);
		thread = new Thread(() -> { loop(); });
		thread.start();
	}
	
	private long calculateInterval(int ps) {
		if(ps <= 0) {
			return ps;
		} else {
			return (long) (Time.SECOND / ps);
		}
	}
	
	private static List<Psilox> runtimeRegistry = new ArrayList<Psilox>();
	private static int nextID = 0;
	
	public static Psilox createRuntime(PsiloxConfig config) {
		if(exclusiveRuntime && Psilox.runtimeRegistry.size() > 0) {
			Log.error("Attempted to create multiple-runtime scenario with exclusiveRuntime=true. Returning existing.");
			return Psilox.get();
		}
		Psilox runtime = new Psilox(config, nextID++);
		runtimeRegistry.add(runtime);
		return runtime;
	}
	
	public static void setExclusiveRuntime(boolean exclusive) {
		Psilox.exclusiveRuntime = exclusive;
	}
	
	public static Psilox get() {
		return runtimeRegistry.get(0);
	}
	
	public static Psilox get(int id) {
		return runtimeRegistry.get(id);
	}
	
	public static void stopAll() {
		for(Psilox p : runtimeRegistry) {
			p.stop();
		}
	}
	
}
