package com.xydium.core;

import java.util.List;
import java.util.Map;

public class Psilox {

	private static Thread runtime;
	private static boolean running;
	private static long tickNumber;
	
	private static Map<Protocol, Integer> runtimeProtocols;
	private static List<Protocol> exitProtocols;
	
	public static void start() {
		Psilox.setRunning(true);
		Psilox.runtime = new Thread(() -> {
			loop();
		});
		Psilox.runtime.start();
	}
	
	public static void stop() {
		for(Protocol p : Psilox.exitProtocols) {
			p.execute();
		}
		Psilox.setRunning(false);
	}
	
	public static void addRuntimeProtocol(Protocol protocol, Integer runEveryXthTick) {
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
		while(Psilox.isRunning()) {
			executeRuntimeProtocols();
		}
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
	
}
