package psilox.math;

public class IntArray2D {

	private int width; 
	private int height;
	private int[] array;
	
	public IntArray2D(int width, int height) {
		this.width = width;
		this.height = height;
		this.array = new int[width * height];
	}
	
}
