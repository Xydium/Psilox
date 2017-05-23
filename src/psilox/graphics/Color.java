package psilox.graphics;

public class Color {

	public static final Color RED = new Color(255, 0, 0, 255);
	public static final Color GREEN = new Color(0, 255, 0, 255);
	public static final Color BLUE = new Color(0, 0, 255, 255);
	public static final Color YELLOW = new Color(255, 255, 0, 255);
	public static final Color CYAN = new Color(0, 255, 255, 255);
	public static final Color MAGENTA = new Color(255, 0, 255, 255);
	public static final Color BLACK = new Color(0, 0, 0, 255);
	public static final Color WHITE = new Color(255, 255, 255, 255);
	public static final Color DARK_GRAY = new Color(.25f, .25f, .25f, 1.0f);
	public static final Color MED_GRAY = new Color(.5f, .5f, .5f, 1.0f);
	public static final Color LIGHT_GRAY = new Color(.75f, .75f, .75f, 1.0f);
	public static final Color CLEAR = new Color(0, 0, 0, 0);
	public static final Color BROWN = new Color(165, 42, 42, 255);
	public static final Color ORANGE = new Color(255, 150, 0);
	
	public final float r, g, b, a;
	
	/**
	 * Constructs a new Color object with
	 * the given rgba values as floats
	 * between 0 and 1.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	/**
	 * Constructs a new Color object with
	 * the given rgb values as floats between
	 * 0 and 1, with an a value of 1.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public Color(float r, float g, float b) {
		this(r, g, b, 1);
	}

	/**
	 * Constructs a new Color object with
	 * the given rgba values as ints
	 * from 0 to 255, converted to floats
	 * between 0 and 1.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public Color(int r, int g, int b, int a) {
		this(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
	}
	
	/**
	 * Constructs a new Color object with
	 * the given rgb values as ints between
	 * 0 and 255, converted to float values
	 * between 0 and 1, and with an a value
	 * of 1.
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public Color(int r, int g, int b) {
		this(r, g, b, 255);
	}
	
	/**
	 * Constructs a new Color object with the
	 * given hexadecimal rgba color formatted as
	 * 0xRRGGBBAA.
	 * 
	 * @param hex
	 */
	public Color(int hex) {
		this((hex >> 24) & 0xFF, (hex >> 16) & 0xFF, (hex >> 8) & 0xFF, hex & 0xFF);
	}
	
	/**
	 * Returns a new Color object with the same
	 * values except for the specified r.
	 * 
	 * @param r
	 * @return
	 */
	public Color rAdj(float r) {
		return new Color(r, g, b, a);
	}
	
	/**
	 * Returns a new Color object with the same
	 * values except for the specified g.
	 * 
	 * @param g
	 * @return
	 */
	public Color gAdj(float g) {
		return new Color(r, g, b, a);
	}
	
	/**
	 * Returns a new Color object with the same
	 * values except for the specified b.
	 * 
	 * @param b
	 * @return
	 */
	public Color bAdj(float b) {
		return new Color(r, g, b, a);
	}
	
	/**
	 * Returns a new Color object with the same
	 * values except for the specified a.
	 * 
	 * @param a
	 * @return
	 */
	public Color aAdj(float a) {
		return new Color(r, g, b, a);
	}
	
	/**
	 * Returns an array of floats four times
	 * the length of the passed color array,
	 * in the form {R, G, B, A, R, G, B, A...}. This
	 * is for use with OpenGL and not meant for end-user
	 * use.
	 * 
	 * @param colors
	 * @return
	 */
	public static float[] toFloatArray(Color[] colors) { 
		float[] res = new float[colors.length * 4];
		int i = 0;
		for(Color v : colors) {
			res[i] = v.r;
			i++;
			res[i] = v.g;
			i++;
			res[i] = v.b;
			i++;
			res[i] = v.a;
			i++;
		}
		return res;
	}
	
	/**
	 * Returns a byte of a given hex color. For example,
	 * if data is 0x8E0000FF and b is 3, it will return
	 * 0xFF.
	 * 
	 * @param data
	 * @param b
	 * @return
	 */
	public static byte getByte(int data, int b) {
		return (byte) ((data >> (8 * b)) & 0xFF);
	}
	
	/**
	 * Returns a hex code from the given four byte values.
	 * 
	 * @param a
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static int byteColor(int a, int r, int g, int b) {
		return (a << 24) | (r << 16) | (g << 8) | b; 
	}
	
	/**
	 * Creates a color object using the given
	 * hue, saturation, brightness, and alpha values.
	 * 
	 * @param hue
	 * @param saturation
	 * @param brightness
	 * @param alpha
	 * @return
	 */
	public static Color hsba(float hue, float saturation, float brightness, int alpha) {
		int r = 0, g = 0, b = 0;
		if (saturation == 0) {
		    r = g = b = (int) (brightness * 255.0f + 0.5f);
		} else {
		    float h = (hue - (float)Math.floor(hue)) * 6.0f;
		    float f = h - (float)java.lang.Math.floor(h);
		    float p = brightness * (1.0f - saturation);
		    float q = brightness * (1.0f - saturation * f);
		    float t = brightness * (1.0f - (saturation * (1.0f - f)));
		    switch ((int) h) {
		    case 0:
		        r = (int) (brightness * 255.0f + 0.5f);
		        g = (int) (t * 255.0f + 0.5f);
		        b = (int) (p * 255.0f + 0.5f);
		        break;
		    case 1:
		        r = (int) (q * 255.0f + 0.5f);
		        g = (int) (brightness * 255.0f + 0.5f);
		        b = (int) (p * 255.0f + 0.5f);
		        break;
		    case 2:
		        r = (int) (p * 255.0f + 0.5f);
		        g = (int) (brightness * 255.0f + 0.5f);
		        b = (int) (t * 255.0f + 0.5f);
		        break;
		    case 3:
		        r = (int) (p * 255.0f + 0.5f);
		        g = (int) (q * 255.0f + 0.5f);
		        b = (int) (brightness * 255.0f + 0.5f);
		        break;
		    case 4:
		        r = (int) (t * 255.0f + 0.5f);
		        g = (int) (p * 255.0f + 0.5f);
		        b = (int) (brightness * 255.0f + 0.5f);
		        break;
		    case 5:
		        r = (int) (brightness * 255.0f + 0.5f);
		        g = (int) (p * 255.0f + 0.5f);
		        b = (int) (q * 255.0f + 0.5f);
		        break;
		    }
		}
		return new Color(r, g, b, alpha);
	}
	
}
