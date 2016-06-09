package com.xydium.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xydium.rendering.Draw;
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
	
	private static final long TICK_LENGTH = 1_000_000_000 / 60;

	private static Thread runtime;
	private static boolean running;
	private static long tickNumber;
	
	private static Map<Protocol, Integer> runtimeProtocols;
	private static List<Protocol> exitProtocols;
	
	private static Window window;
	
	/**
	 * Used to enable additional features within Psilox,
	 * such as protocols.
	 */
	public static void preInit() {
		if(exitProtocols == null) {
			Psilox.createProtocolLists();
		}
	}
	
	/**
	 * Starts the engine and loop.
	 */
	public static void start() {
		Psilox.preInit();
		Psilox.window = new Window(128, 72, 5, "Test");
		Draw.initDraw();
		Psilox.setRunning(true);
		Psilox.runtime = new Thread(() -> {
			Test.start();
			loop();
		});
		Psilox.runtime.start();
	}
	
	/**
	 * Stops the loop before its next tick.
	 */
	public static void stop() {
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
	
	private static void loop() {
		long lastTick = 0;
		
		while(Psilox.isRunning()) {
			Psilox.waitForTick(lastTick);
			
			lastTick = Time.now();
			Psilox.tickNumber++;
			
			//update all nodes/components in SceneTree
			Test.update();
			//Clean up last frame's layers
			Draw.clear();
			//render all nodes/components in SceneTree to Layers
			Test.render();
			//Draw finished rendering into the Window
			Draw.flatten();
			Psilox.window.drawFrame(Draw.getCurrentFrame());
			//Execute Requested User Protocols, if any
			if(!Psilox.runtimeProtocols.isEmpty()) executeRuntimeProtocols();
		}
		
		executeExitProtocols();
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
		Psilox.window.destroy();
	}
	
	private static void waitForTick(long lastTick) {
		while(Time.since(lastTick) < Psilox.TICK_LENGTH) {}
	}
	
	private static void createProtocolLists() {
		Psilox.runtimeProtocols = new HashMap<Protocol, Integer>();
		Psilox.exitProtocols = new ArrayList<Protocol>();
	}
	
}
