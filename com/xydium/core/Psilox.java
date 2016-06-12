package com.xydium.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xydium.input.Input;
import com.xydium.rendering.Draw;
import com.xydium.resources.GlobalAudio;
import com.xydium.utility.Log;
import com.xydium.utility.Log.LogLevel;
import com.xydium.utility.Protocol;
import com.xydium.utility.Time;

/**
 * 
 * Psilox runtime, initialized from end user defined
 * main method. Contains primary game loop for updating
 * and rendering, as well as executing user-defined protocols.
 * Entirely static access.
 * 
 * @author Xydium
 *
 */
public class Psilox {
	
	private static EngineConfig engineConfig;
	
	private static long TICK_LENGTH = 1_000_000_000;

	private static Thread runtime;
	private static boolean running;
	private static long tickNumber;
	
	private static Map<Protocol, Integer> runtimeProtocols;
	private static List<Protocol> exitProtocols;
	
	private static Window window;
	
	private static Scene scene;
	
	/**
	 * Starts the engine and loop.
	 */
	public static void start() {
		Log.internal("Psilox started.");
		Log.internal("Loading Engine Config...");
		Psilox.engineConfig = new EngineConfig();
		Log.internal("Engine Config Loaded.");
		
		Log.setConsoleEnabled(Psilox.engineConfig.getBoolean("consoleenabled"));
		Log.setLogLevel(LogLevel.values()[Psilox.engineConfig.getInt("consoledetail")]);
		Log.internal(String.format("LogLevel Set To %s. Console Enabled is %s.", Log.getLogLevel().name(), Log.getConsoleEnabled()));
		
		Psilox.TICK_LENGTH /= Psilox.engineConfig.getInt("tickspersec");
		Log.internal(String.format("Tick Rate Set To %s With Duration Of %s NS", Psilox.engineConfig.getInt("tickspersec"), TICK_LENGTH));
		
		Log.internal("Creating Protocol Lists...");
		Psilox.createProtocolLists();
		
		Log.internal("Creating Window...");
		Psilox.window = new Window(Psilox.engineConfig.getInt("width"), 
								   Psilox.engineConfig.getInt("height"), 
								   Psilox.engineConfig.getDouble("scale"),
								   Psilox.engineConfig.getString("title"));
		Log.internal("Window Created.");
		Log.internal(Psilox.window.toString());
		
		Log.internal("Initializing Draw...");
		Draw.initDraw();
		Log.internal("Draw Initialized.");
		
		Log.internal("Initializing Audio...");
		GlobalAudio.initAudio();
		Log.internal("Audio initialized.");
		
		Log.internal("Loading Main Scene...");
		try {
			Psilox.setScene((Scene) (Class.forName(Psilox.engineConfig.getString("mainscene")).newInstance()));
		} catch (Exception e) {
			Log.error("Something went wrong while loading mainscene.");
			Log.error(e);
			Psilox.stop();
		}
		Log.internal(String.format("Scene %s loaded. (%s)", Psilox.currentScene().getClass().getSimpleName(), Psilox.currentScene().getClass().getName()));
		
		Psilox.setRunning(true);
		Log.internal("Running Psilox...");
		Psilox.runtime = new Thread(() -> {
			loop();
		});
		Psilox.runtime.start();
	}
	
	/**
	 * Stops the loop before its next tick.
	 */
	public static void stop() {
		Log.internal("Stopping Psilox...");
		Psilox.setRunning(false);
	}
	
	/**
	 * Puts a new Runtime Protocol into the map
	 * for execution in the main loop.  
	 * 
	 * @param protocol the protocol to run
	 * @param runEveryXthTick executes the protocol on this tick remainder
	 */
	public static void addRuntimeProtocol(Protocol protocol, int runEveryXthTick) {
		Psilox.runtimeProtocols.put(protocol, runEveryXthTick);
	}
	
	/**
	 * Puts the new Runtime Protocol into the map
	 * with no tick delay. 
	 * 
	 * @param protocol
	 */
	public static void addRuntimeProtocol(Protocol protocol) {
		Psilox.addRuntimeProtocol(protocol, 1);
	}
	
	/**
	 * Adds an Exit Protocol to be run
	 * by the engine after calling stop.
	 * 
	 * @param protocol the protocol to run at exit
	 */
	public static void addExitProtocol(Protocol protocol) {
		Psilox.exitProtocols.add(protocol);
	}
	
	public static void rescale(double scale) {
		Psilox.window.rescale(scale);
	}
	
	public static double currentWindowScale() {
		return Psilox.window.getScale();
	}
	
	/**
	 * @return if the engine is running
	 */
	public static boolean isRunning() {
		return Psilox.running;
	}
	
	/**
	 * @return number of ticks elapsed since start
	 */
	public static long getTickNumber() {
		return Psilox.tickNumber;
	}

	/**
	 * @return width of rendering buffer
	 */
	public static int windowWidth() {
		return Psilox.window.getWidth();
	}
	
	/**
	 * @return height of rendering buffer
	 */
	public static int windowHeight() {
		return Psilox.window.getHeight();
	}
	
	/**
	 * @return width of jframe, accounting for scale
	 */
	public static int frameWidth() {
		return (int) (Psilox.windowWidth() * Psilox.window.getScale());
	}
	
	/**
	 * @return height of jframe, account for scale
	 */
	public static int frameHeight() {
		return (int) (Psilox.windowHeight() * Psilox.window.getScale());
	}
	
	public static Scene currentScene() {
		return Psilox.scene;
	}
	
	public static Scene getScene() {
		return scene;
	}
	
	public static void setScene(Scene scene) {
		if(Psilox.scene != null) Psilox.scene.deactivate();
		Draw.dumpDrawingLayers();
		Psilox.scene = scene;
		Psilox.scene.activate();
	}
	
	private static void loop() {
		Log.internal("Psilox is now running.");
		long lastTick = 0;
		
		while(Psilox.isRunning()) {
			Psilox.waitForTick(lastTick);
			
			lastTick = Time.now();
			Psilox.tickNumber++;
			
			Psilox.scene.update();
			Draw.clear();
			Psilox.scene.render();
			Draw.flatten();
			Psilox.window.drawFrame(Draw.getCurrentFrame());
			if(Psilox.runtimeProtocols != null) executeRuntimeProtocols();
		}
		
		Log.internal("Escaped Main Loop.");
		Log.internal("Dumping Input Listeners...");
		Input.dumpListeners();
		Log.internal("Input Listeners Dumped.");
		Log.internal("Running Exit Protocols...");
		executeExitProtocols();
		Log.internal("Exit Protocols Completed.");
		Log.internal("Shutting down audio...");
		GlobalAudio.shutdown();
		Log.internal("Audio shutdown.");
		Log.internal("Psilox stopped.");
		Psilox.window.destroy();
	}
	
	private static void setRunning(boolean running) {
		Psilox.running = running;
	}
	
	private static void executeRuntimeProtocols() {
		Object[] protocols = Psilox.runtimeProtocols.keySet().toArray();
		Protocol p;
		int pDelay;
		for(int px = 0; px < protocols.length; px++) {
			p = (Protocol) protocols[px];
			pDelay = Psilox.runtimeProtocols.get(p);
			if(pDelay == 1 || Psilox.getTickNumber() % pDelay == 0) {
				p.execute();
			}
		}
	}
	
	private static void executeExitProtocols() {
		for(Protocol p : Psilox.exitProtocols) {
			p.execute();
		}
	}
	
	private static void waitForTick(long lastTick) {
		while(Time.since(lastTick) < Psilox.TICK_LENGTH) {}
	}
	
	private static void createProtocolLists() {
		if(Psilox.engineConfig.getBoolean("usingruntimeprotocols")) {
			Psilox.runtimeProtocols = new HashMap<Protocol, Integer>();
			Log.internal("Created Runtime Protocol List.");
		}
		Psilox.exitProtocols = new ArrayList<Protocol>();
		Log.internal("Created Exit Protocol List.");
	}
	
}
