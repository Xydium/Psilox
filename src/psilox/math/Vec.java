package psilox.math;

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
	
	public float x, y, z;
	
	public Vec(double x, double y, double z) {
		this((float) x, (float) y, (float) z);
	}
	
	public Vec(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec(double x, double y) {
		this((float) x, (float) y);
	}
	
	public Vec(float x, float y) {
		this(x, y, 0);
	}
	
	public Vec(double n) {
		this((float) n);
	}
	
	public Vec(float n) {
		this(n, n, 0);
	}
	
	public Vec(Vec o) {
		this(o.x, o.y, o.z);
	}
	
	public Vec sum(Vec o) {
		return new Vec(x + o.x, y + o.y, z + o.z);
	}
	
	public void add(Vec o) {
		x += o.x;
		y += o.y;
		z += o.z;
	}
	
	public Vec dif(Vec o) {
		return new Vec(x - o.x, y - o.y, z - o.z);
	}
	
	public void sub(Vec o) {
		x -= o.x;
		y -= o.y;
		z -= o.z;
	}
	
	public Vec pro(Vec o) {
		return new Vec(x * o.x, y * o.y, z * o.z);
	}
	
	public void mul(Vec o) {
		x *= o.x;
		y *= o.y;
		z *= o.z;
	}
	
	public void mul(float o) {
		x *= o;
		y *= o;
		z *= o;
	}
	
	public Vec scl(float d) {
		return new Vec(x * d, y * d, z * d);
	}
	
	public Vec quo(Vec o) {
		return new Vec(x / o.x, y / o.y, z / o.z);
	}
	
	public void div(Vec o) {
		x /= o.x;
		y /= o.y;
		z /= o.z;
	}
	
	public void div(float o) {
		x *= o;
		y *= o;
		z *= o;
	}
	
	public Vec half() {
		return scl(.5f);
	}
	
	public Vec rot(float theta) {
		theta = (float) Math.toRadians(theta);
		double sin = Math.sin(theta);
		double cos = Math.cos(theta);
		return new Vec(x * cos - y * sin, x * sin + y * cos, z);
	}
	
	public Vec clm(float mag) {
		if(mag() > mag) {
			return nrm().scl(mag);
		}
		return this;
	}
	
	public float mag() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public float hp3() {
		return x * x + y * y + z * z;
	}
	
	public float hp2() {
		return x * x + y * y;
	}
	
	public Vec nrm() {
		float mag = mag();
		if(mag == 0) return Vec.ZERO; 
		return new Vec(x / mag, y / mag, z / mag);
	}
	
	public boolean btn(Vec o, Vec e) {
		return x >= o.x && y >= o.y && x <= e.x && y <= e.y;
	}
	
	public float dst(Vec o) {
		return dif(o).mag();
	}
	
	public float ang() {
		return (float) Math.toDegrees(Math.atan2(y, x));
	}
	
	public float dot(Vec o) {
		return x * o.x + y * o.y + z * o.z;
	}

	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setXY(float n) {
		this.x = n;
		this.y = n;
	}
	
	public void set(float n) {
		this.x = n;
		this.y = n;
		this.z = n;
	}
	
	public void set(Vec o) {
		this.x = o.x;
		this.y = o.y;
		this.z = o.z;
	}
	
	public static Vec angMag(float angle, float mag) {
		angle = (float) Math.toRadians(angle);
		Vec res = new Vec(Math.cos(angle), Math.sin(angle));
		res.mul(mag);
		return res;
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
	
	public boolean equals(Vec o) {
		return x == o.x && y == o.y && z == o.z;
	}
	
	public String toString() {
		return String.format("(%.2f, %.2f, %.2f)", x, y, z);
	}
	
}
