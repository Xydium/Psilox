package com.xydium.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xydium.utility.Time;

public class Psilox {

	private static final long TICK_LENGTH = 1_000_000_000 / 60;
	
	private static Thread runtime;
	private static boolean running;
	private static long tickNumber;
	
	private static Map<Protocol, Integer> runtimeProtocols;
	private static List<Protocol> exitProtocols;
	
	public static void preInit() {
		Psilox.createProtocolLists();
	}
	
	public static void start() {
		Psilox.setRunning(true);
		Psilox.runtime = new Thread(() -> {
			loop();
		});
		Psilox.runtime.start();
	}
	
	public static void stop() {
		Psilox.setRunning(false);
	}
	
	public static void addRuntimeProtocol(Protocol protocol, int runEveryXthTick) {
		Psilox.runtimeProtocols.put(protocol, runEveryXthTick);
	}
	
	public static void addRuntimeProtocol(Protocol protocol) {
		Psilox.addRuntimeProtocol(protocol, 1);
	}
	
	public static void addExitProtocol(Protocol protocol) {
		Psilox.exitProtocols.add(protocol);
	}
	
	public static boolean isRunning() {
		return Psilox.running;
	}
	
	public static long getTickNumber() {
		return Psilox.tickNumber;
	}

	private static void loop() {
		long lastTick = 0;
		
		while(Psilox.isRunning()) {
			Psilox.waitForTick(lastTick);
			
			lastTick = Time.now();
			Psilox.tickNumber++;
			
			//Execute Engine Runtime

			//update all nodes/components in SceneTree
			//render all nodes/components in SceneTree to Layers
			//Compress Layers to single image
			//Draw image into window
			
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
	}
	
	private static void waitForTick(long lastTick) {
		while(Time.since(lastTick) < Psilox.TICK_LENGTH) {}
	}
	
	private static void createProtocolLists() {
		Psilox.runtimeProtocols = new HashMap<Protocol, Integer>();
		Psilox.exitProtocols = new ArrayList<Protocol>();
	}
	
}
