package com.xydium.psilox.math;


public class Vec3 {

	public static final Vec3 X_UNIT = new Vec3(1, 0, 0);
	public static final Vec3 Y_UNIT = new Vec3(0, 1, 0);
	public static final Vec3 Z_UNIT = new Vec3(0, 0, 1);
	public static final Vec3 ONE = new Vec3(1, 1, 1);
	public static final Vec3 ZERO = new Vec3(0, 0, 0);
	public static final Vec3 TWO_D = new Vec3(1, 1, 0);
	
	public final float x, y, z;
	
	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3(float x, float y) {
		this(x, y, 0);
	}
	
	public Vec3(float n) {
		this(n, n, n);
	}
	
	public Vec3 sum(Vec3 o) {
		return new Vec3(x + o.x, y + o.y, z + o.z);
	}
	
	public Vec3 dif(Vec3 o) {
		return new Vec3(x - o.x, y - o.y, z - o.z);
	}
	
	public Vec3 pro(Vec3 o) {
		return new Vec3(x * o.x, y * o.y, z * o.z);
	}
	
	public Vec3 sca(float d) {
		return new Vec3(x * d, y * d, z * d);
	}
	
	public Vec3 quo(Vec3 o) {
		return new Vec3(x / o.x, y / o.y, z / o.z);
	}
	
	public static float[] toFloatArray(Vec3[] vertices) { 
		float[] res = new float[vertices.length * 3];
		int i = 0;
		for(Vec3 v : vertices) {
			res[i] = v.x;
			i++;
			res[i] = v.y;
			i++;
			res[i] = v.z;
			i++;
		}
		return res;
	}
	
}
