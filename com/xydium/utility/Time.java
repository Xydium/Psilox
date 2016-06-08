package com.xydium.utility;

public class Time {

	public static long now() {
		return System.nanoTime();
	}
	
	public static long since(long time) {
		return System.nanoTime() - time;
	}
	
}
