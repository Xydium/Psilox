package psilox.math;

public class Rect {

	public static final Rect UNIT = new Rect(0, 0, 1, 1);
	
	public final float x, y, w, h;
	
	public Rect(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
}
