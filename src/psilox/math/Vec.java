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
	
	/**
	 * Constructs a new Vec with the given values
	 * casted down to floats.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vec(double x, double y, double z) {
		this((float) x, (float) y, (float) z);
	}
	
	/**
	 * Constructs a new Vec with the given values.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vec(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Constructs a new Vec with the given values
	 * casted down to floats, with a Z of zero.
	 * 
	 * @param x
	 * @param y
	 */
	public Vec(double x, double y) {
		this((float) x, (float) y);
	}
	
	/**
	 * Constructs a new Vec with the given values
	 * and a Z of zero.
	 * 
	 * @param x
	 * @param y
	 */
	public Vec(float x, float y) {
		this(x, y, 0);
	}
	
	/**
	 * Constructs a new Vec with the given value
	 * casted down to a float and set as the X and Y
	 * with a Z of zero.
	 * 
	 * @param n
	 */
	public Vec(double n) {
		this((float) n);
	}
	
	/**
	 * Constructs a new Vec with the given value,
	 * setting X and Y with a Z of zero.
	 * 
	 * @param n
	 */
	public Vec(float n) {
		this(n, n, 0);
	}
	
	/**
	 * Constructs a new Vec with X, Y, and Z at zero.
	 */
	public Vec() {
		this(0);
	}
	
	/**
	 * Constructs a new Vec with the exact
	 * same components as Vec o.
	 * 
	 * @param o
	 */
	public Vec(Vec o) {
		this(o.x, o.y, o.z);
	}
	
	/**
	 * Constructs a new Vec with the summed
	 * components of this Vec and Vec o.
	 * 
	 * @param o
	 * @return
	 */
	public Vec sum(Vec o) {
		return new Vec(x + o.x, y + o.y, z + o.z);
	}
	
	/**
	 * Modifies this Vec to be the sum of itself
	 * and Vec o.
	 * 
	 * @param o
	 */
	public void add(Vec o) {
		x += o.x;
		y += o.y;
		z += o.z;
	}
	
	/**
	 * Constructs a new Vec with the subtracted
	 * components of this Vec and Vec o.
	 * 
	 * @param o
	 * @return
	 */
	public Vec dif(Vec o) {
		return new Vec(x - o.x, y - o.y, z - o.z);
	}
	
	/**
	 * Modifies this Vec to be the difference
	 * of itself and Vec o.
	 * 
	 * @param o
	 */
	public void sub(Vec o) {
		x -= o.x;
		y -= o.y;
		z -= o.z;
	}
	
	/**
	 * Contructs a new Vec with the product
	 * of the components of this Vec and Vec o.
	 * 
	 * @param o
	 * @return
	 */
	public Vec pro(Vec o) {
		return new Vec(x * o.x, y * o.y, z * o.z);
	}
	
	/**
	 * Modifies this Vec to be the component product
	 * of this Vec and Vec o.
	 * 
	 * @param o
	 */
	public void mul(Vec o) {
		x *= o.x;
		y *= o.y;
		z *= o.z;
	}
	
	/**
	 * Modifies this Vec to be the product of this
	 * Vec and Scalar o.
	 * 
	 * @param o
	 */
	public void mul(float o) {
		x *= o;
		y *= o;
		z *= o;
	}
	
	/**
	 * Constructs a new Vec that is the product
	 * of this Vec and a Scalar o.
	 * 
	 * @param d
	 * @return
	 */
	public Vec scl(float d) {
		return new Vec(x * d, y * d, z * d);
	}
	
	/**
	 * Constructs a new Vec that is the component
	 * quotient of this Vec and Vec o.
	 * 
	 * @param o
	 * @return
	 */
	public Vec quo(Vec o) {
		return new Vec(x / o.x, y / o.y, z / o.z);
	}
	
	/**
	 * Constructs a new Vec that is the component
	 * quotient of this Vec and a scalar o.
	 * 
	 * @param d
	 * @return
	 */
	public Vec quo(float d) {
		return new Vec(x / d, y / d, z / d);
	}
	
	/**
	 * Modifies this Vec to be the component
	 * quotient of this Vec and Vec o.
	 * 
	 * @param o
	 */
	public void div(Vec o) {
		x /= o.x;
		y /= o.y;
		z /= o.z;
	}
	
	/**
	 * Modifies this Vec to be the component
	 * quotient of this Vec and scalar o.
	 * 
	 * @param o
	 */
	public void div(float o) {
		x /= o;
		y /= o;
		z /= o;
	}
	
	/**
	 * Returns the result of scl(.5f).
	 * 
	 * @return
	 */
	public Vec half() {
		return scl(.5f);
	}
	
	/**
	 * Constructs a Vec that is this Vec 
	 * rotated by degrees theta.
	 * 
	 * @param theta
	 * @return
	 */
	public Vec rot(float theta) {
		theta = (float) Math.toRadians(theta);
		double sin = Math.sin(theta);
		double cos = Math.cos(theta);
		return new Vec(x * cos - y * sin, x * sin + y * cos, z);
	}
	
	/**
	 * Constructs a new Vec that is this Vec
	 * clamped to magnitude mag.
	 * 
	 * @param mag
	 * @return
	 */
	public Vec clm(float mag) {
		if(mag() > mag) {
			return nrm().scl(mag);
		}
		return this;
	}
	
	/**
	 * Returns the magnitude of this Vec.
	 * 
	 * @return
	 */
	public float mag() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	/**
	 * Returns x^2 + y^2 + z^2.
	 * 
	 * @return
	 */
	public float hp3() {
		return x * x + y * y + z * z;
	}
	
	/**
	 * Returns x^2 + y^2. 
	 * 
	 * @return
	 */
	public float hp2() {
		return x * x + y * y;
	}

	/**
	 * Constructs a new Vec that is this Vec
	 * casted down to integers.
	 * 
	 * @return
	 */
	public Vec ic() {
		return new Vec((int) x, (int) y, (int) z);
	}
	
	/**
	 * Constructs a new Vec that is the x-component
	 * vector of this Vec.
	 * 
	 * @return
	 */
	public Vec xc() {
		return new Vec(x, 0, 0);
	}
	
	/**
	 * Constructs a new Vec that is the y-component
	 * vector of this Vec.
	 * 
	 * @return
	 */
	public Vec yc() {
		return new Vec(0, y, 0);
	}
	
	/**
	 * Constructs a new Vec that is the z-component
	 * vector of this Vec.
	 * 
	 * @return
	 */
	public Vec zc() {
		return new Vec(0, 0, z);
	}
	
	/**
	 * Contructs a Vec that is the normalized version
	 * of this Vec, with the same angle but a mag
	 * of 1.
	 * 
	 * @return
	 */
	public Vec nrm() {
		float mag = mag();
		if(mag == 0) return Vec.ZERO; 
		return new Vec(x / mag, y / mag, z / mag);
	}
	
	/**
	 * Returns whether this Vec is inside the
	 * rectangular area formed by Vec o and Vec e.
	 * 
	 * @param o
	 * @param e
	 * @return
	 */
	public boolean btn(Vec o, Vec e) {
		return x >= o.x && y >= o.y && x <= e.x && y <= e.y;
	}
	
	/**
	 * Returns the distance from this Vec to Vec o,
	 * where this Vec and Vec o are treated as coordinates.
	 * 
	 * @param o
	 * @return
	 */
	public float dst(Vec o) {
		return dif(o).mag();
	}
	
	/**
	 * Returns the angle in degrees of this Vec.
	 * 
	 * @return
	 */
	public float ang() {
		return (float) Math.toDegrees(Math.atan2(y, x));
	}
	
	/**
	 * Returns the dot product of this Vec and Vec o.
	 * 
	 * @param o
	 * @return
	 */
	public float dot(Vec o) {
		return x * o.x + y * o.y + z * o.z;
	}

	/**
	 * Sets all components of this Vec.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Sets the X and Y components of this Vec.
	 * 
	 * @param x
	 * @param y
	 */
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Sets the X and Y components of this Vec to n.
	 * 
	 * @param n
	 */
	public void setXY(float n) {
		this.x = n;
		this.y = n;
	}
	
	/**
	 * Sets all components of this Vec to n.
	 * 
	 * @param n
	 */
	public void set(float n) {
		this.x = n;
		this.y = n;
		this.z = n;
	}
	
	/**
	 * Sets all components of this Vec to
	 * the corresponding components of Vec o.
	 * 
	 * @param o
	 */
	public void set(Vec o) {
		this.x = o.x;
		this.y = o.y;
		this.z = o.z;
	}
	
	/**
	 * Reflects this vector against the provided vector.
	 * For example, if a vec points toward a vertical Vec at
	 * an angle of -45 or 315 degrees, the returned vector will
	 * be a vector of the same magnitude of angle 45 degrees. The above parameter
	 * controls which side o is reflected off of, and does not necessarily correspond
	 * to above or below a vector. When false, for vertical vectors it will flip the normal 
	 * horizontally. For horizontal vectors it will flip the normal vertically.
	 * 
	 * @param above
	 * @param o
	 */
	public Vec rfl(boolean above, Vec o) {
		Vec normal = new Vec(y, x).nrm();
		if(!above) {
			normal.mul(-1);
		}
		return o.dif(normal.scl(2 * normal.dot(o)));
	}
	
	/**
	 * Contructs a new Vec with the specified
	 * angle and magnitude.
	 * 
	 * @param angle
	 * @param mag
	 * @return
	 */
	public static Vec angMag(float angle, float mag) {
		angle = (float) Math.toRadians(angle);
		Vec res = new Vec(Math.cos(angle), Math.sin(angle));
		res.mul(mag);
		return res;
	}
	
	/**
	 * Generates a float array from the given array of Vecs
	 * in format x, y, z, x, y, z, etc.
	 * 
	 * @param vertices
	 * @return
	 */
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
	
	/**
	 * Returns whether this Vec's components
	 * equate to Vec o's components;
	 * 
	 * @param o
	 * @return
	 */
	public boolean equals(Vec o) {
		return x == o.x && y == o.y && z == o.z;
	}
	
	/**
	 * Returns a string in the format (x, y, z) to 
	 * two decimal places.
	 */
	public String toString() {
		return String.format("(%.2f, %.2f, %.2f)", x, y, z);
	}
	
}
