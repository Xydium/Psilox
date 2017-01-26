package com.xydium.psilox.core;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.xydium.psilox.math.Vec3;
import com.xydium.psilox.rendering.Color;
import com.xydium.psilox.rendering.CombinedBuffer;
import com.xydium.psilox.rendering.Primitives;
import com.xydium.psilox.rendering.Render;
import com.xydium.psilox.utilities.Log;
import com.xydium.psilox.utilities.Time;

public class Psilox {

	private static boolean exclusiveRuntime = true;

	private int id;
	
	public final PsiloxConfig config;
	
	private long updateInterval;
	private long renderInterval;
	
	private Thread thread;
	private boolean running;
	private boolean clearScreen;
	private long tick;
	private float deltaTime;
	
	private Window window;
	
	private Psilox(PsiloxConfig config) {
		this.config = config;
	}
	
	public void start(/*Node mainNode*/) {
		if(running()) return;
		initLog();
		initIntervals();
		initWindow();
		initThread();
		//tree.getRoot().addChild(mainNode);
	}
	
	public void stop() {
		setRunning(false);
	}
	
	public void update() {
		//tree.update();
		tick++;
	}
	
	private CombinedBuffer shape = new CombinedBuffer(Primitives.C_UTRI, new Color[] {
			new Color(1, 0, 0, 1),
			new Color(0, 1, 0, 1),
			new Color(0, 0, 1, 1),
	});
	
	public void render() {
		if(clearScreen) Render.clear();
		Render.setTransform(new Vec3(100, 100), tick % 360, new Vec3(50, 50));
		shape.render(GL2.GL_TRIANGLES);
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
	
	public Window window() {
		return window;
	}
	
	private void loop() {
		long lastUpdate = Time.now() - updateInterval;
		long lastRender = Time.now() - renderInterval;
		
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
		}
		
		exit();
	}
	
	private void exit() {
		//input.dumpListeners();
		//audio.destroy();
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
		window = new Window(config.title, config.width, config.height, this);
		window.setup();
		Render.clearBufferBit = GL.GL_COLOR_BUFFER_BIT;
	}
	
	private void initThread() {
		setRunning(true);
		thread = new Thread(new Runnable() {
			public void run() {
				loop();
			}			
		});
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
	
	public static Psilox createRuntime(PsiloxConfig config) {
		if(exclusiveRuntime && Psilox.runtimeRegistry.size() > 0) {
			Log.error("Attempted to create multiple-runtime scenario with exclusiveRuntime=true. Returning existing.");
			return Psilox.get();
		}
		Psilox runtime = new Psilox(config);
		runtimeRegistry.add(runtime);
		runtime.id = runtimeRegistry.indexOf(runtime);
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
	
}
