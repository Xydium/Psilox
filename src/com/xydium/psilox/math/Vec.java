package com.xydium.psilox.math;


public class Vec {

	public static final Vec X_UNIT = new Vec(1, 0, 0);
	public static final Vec Y_UNIT = new Vec(0, 1, 0);
	public static final Vec Z_UNIT = new Vec(0, 0, 1);
	public static final Vec ONE = new Vec(1, 1, 1);
	public static final Vec ZERO = new Vec(0, 0, 0);
	public static final Vec TWO_D = new Vec(1, 1, 0);
	public static final Vec UP = new Vec(0, 1);
	public static final Vec DOWN = new Vec(0, -1);
	public static final Vec LEFT = new Vec(-1, 0);
	public static final Vec RIGHT = new Vec(1, 0);
	
	public final float x, y, z, mag;
	
	public Vec(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.mag = (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public Vec(float x, float y) {
		this(x, y, 0);
	}
	
	public Vec(float n) {
		this(n, n, n);
	}
	
	public Vec sum(Vec o) {
		return new Vec(x + o.x, y + o.y, z + o.z);
	}
	
	public Vec dif(Vec o) {
		return new Vec(x - o.x, y - o.y, z - o.z);
	}
	
	public Vec pro(Vec o) {
		return new Vec(x * o.x, y * o.y, z * o.z);
	}
	
	public Vec scl(float d) {
		return new Vec(x * d, y * d, z * d);
	}
	
	public Vec quo(Vec o) {
		return new Vec(x / o.x, y / o.y, z / o.z);
	}
	
	public float mag() {
		return mag;
	}
	
	public Vec nrm() {
		if(mag == 0) return Vec.ZERO; 
		return new Vec(x / mag, y / mag, z / mag);
	}
	
	public float dot(Vec o) {
		return x * o.x + y * o.y + z * o.z;
	}
	
	public static float[] toFloatArray(Vec[] vertices) { 
		float[] res = new float[vertices.length * 3];
		int i = 0;
		for(Vec v : vertices) {
			res[i] = v.x;
			i++;
			res[i] = v.y;
			i++;
			res[i] = v.z;
			i++;
		}
		return res;
	}
	
	public String toString() {
		return String.format("(%.2f, %.2f, %.2f)", x, y, z);
	}
	
}
