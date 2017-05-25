package psilox.math;

import java.nio.FloatBuffer;

import psilox.utility.Buffers;

public class Mat4 {

	public static final int SIZE = 4 * 4;
	public float[] elements = new float[SIZE];
	
	public Mat4() {}
	
	public static Mat4 identity() {
		Mat4 result = new Mat4();
		result.elements[0 + 0 * 4] = 1.0f;
		result.elements[1 + 1 * 4] = 1.0f;
		result.elements[2 + 2 * 4] = 1.0f;
		result.elements[3 + 3 * 4] = 1.0f;
		
		return result;
	}
	
	public static Mat4 orthographic(float left, float right, float bottom, float top, float near, float far) {
		Mat4 result = identity();
		
		result.elements[0 + 0 * 4] = 2.0f / (right - left);

		result.elements[1 + 1 * 4] = 2.0f / (top - bottom);

		result.elements[2 + 2 * 4] = 2.0f / (near - far);
		
		result.elements[0 + 3 * 4] = (left + right) / (left - right);
		result.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
		result.elements[2 + 3 * 4] = (far + near) / (far - near);
		
		return result;
	}
	
	public static Mat4 translate(Vec vector) {
		Mat4 result = identity();
		result.elements[0 + 3 * 4] = vector.x;
		result.elements[1 + 3 * 4] = vector.y;
		result.elements[2 + 3 * 4] = vector.z;
		return result;
	}
	
	public static Mat4 rotate(float angle, Vec p) {
		Mat4 result = identity();
		float r = (float) Math.toRadians(angle);
		float cos = (float) Math.cos(r);
		float sin = (float) Math.sin(r);
		p.z = 0;
		
		result.elements[0 + 0 * 4] = cos;
		result.elements[1 + 0 * 4] = sin;
		
		result.elements[0 + 1 * 4] = -sin;
		result.elements[1 + 1 * 4] = cos;
		
		return result;
	}
	
	public static Mat4 transform(Vec p, float angle, Vec scl) {
		Mat4 result = new Mat4();
		float r = Mathf.rad(angle);
		float cos = Mathf.cos(r);
		float sin = Mathf.sin(r);
		
		result.elements[0 + 0 * 4] = scl.x * cos;
		result.elements[0 + 1 * 4] = scl.y * sin;
		result.elements[0 + 3 * 4] = p.x;
		
		result.elements[1 + 0 * 4] = -scl.x * sin;
		result.elements[1 + 1 * 4] = scl.y * cos;
		result.elements[1 + 3 * 4] = p.y;
		
		result.elements[2 + 3 * 4] = p.z;
		
		result.elements[2 + 2 * 4] = 1;
		result.elements[3 + 3 * 4] = 1;
		
		return result;
	}
	
	private static float r;
	private static float cos;
	private static float sin;
	private static float a, b, c, d;
	public static Mat4 transform(Mat4 parent, Vec p, float angle, Vec scl) {
		Mat4 result = new Mat4();
		r = Mathf.rad(angle);
		cos = Mathf.cos(r);
		sin = Mathf.sin(r);
		
		a = parent.elements[0]; b = parent.elements[1]; c = parent.elements[4]; d = parent.elements[5];
		
		result.elements[0 + 0 * 4] = (a * scl.x * cos) - (c * scl.x * sin);
		result.elements[1 + 0 * 4] = (b * scl.x * cos) - (d * scl.x * sin);
		
		result.elements[0 + 1 * 4] = (a * scl.y * sin) + (c * scl.y * cos);
		result.elements[1 + 1 * 4] = (b * scl.x * sin) + (d * scl.y * cos);
		
		result.elements[0 + 3 * 4] = a * p.x + c * p.y + parent.elements[0 + 3 * 4];
		result.elements[1 + 3 * 4] = b * p.x + d * p.y + parent.elements[1 + 3 * 4];
		result.elements[2 + 3 * 4] = p.z + parent.elements[2 + 3 * 4];
		
		result.elements[2 + 2 * 4] = 1;
		result.elements[3 + 3 * 4] = 1;
		
		return result;
	}
	
	public Mat4 multiply(Mat4 matrix) {
		Mat4 result = new Mat4();
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				float sum = 0.0f;
				for (int e = 0; e < 4; e++) {
					sum += this.elements[x + e * 4] * matrix.elements[e + y * 4]; 
				}			
				result.elements[x + y * 4] = sum;
			}
		}
		return result;
	}
	
	public FloatBuffer toFloatBuffer() {
		return Buffers.createFloatBuffer(elements);
	}
	
}
