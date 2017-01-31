package com.xydium.psilox.math;

public class Random {

	public static int intVal() {
		return (int) doubleVal(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public static int intVal(int max) {
		return (int) doubleVal(0, max);
	}
	
	public static int intVal(int min, int max) {
		return (int) doubleVal(min, max);
	}
	
	public static float floatVal() {
		return (float) doubleVal(Float.MIN_VALUE, Float.MAX_VALUE);
	}
	
	public static float floatVal(float max) {
		return (float) doubleVal(0, max);
	}
	
	public static float floatVal(float min, float max) {
		return (float) doubleVal(min, max);
	}
	
	public static double doubleVal() {
		return doubleVal(Double.MIN_VALUE, Double.MAX_VALUE);
	}
	
	public static double doubleVal(int max) {
		return doubleVal(0, max);
	}
	
	public static double doubleVal(double min, double max) {
		return (Math.random() * (max - min)) + min;
	}
	
}
