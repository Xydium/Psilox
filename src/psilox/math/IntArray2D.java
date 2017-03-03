package psilox.math;

public class IntArray2D {

	public final int width; 
	public final int height;
	public final int[] array;
	
	public IntArray2D(int width, int height) {
		this.width = width;
		this.height = height;
		this.array = new int[width * height];
	}
	
	public int get(int i) {
		return array[i];
	}
	
	public int get(int x, int y) {
		return array[y * width + x];
	}
	
	public void set(int i, int v) {
		array[i] = v;
	}
	
	public void set(int x, int y, int v) {
		array[y * width + x] = v;
	}
	
	public int[][] getArea(int x, int y, int w, int h) {
		int[][] area = new int[w][h];
		for(int xx = x; xx < x + w; xx++) {
			for(int yy = y; yy < y + h; yy++) {
				area[xx][yy] = get(xx, yy);
			}
		}
		return area;
	}
	
	public void iterate(ArrayIterator it) {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				array[y * width + x] = it.iterate(x, y);
			}
		}
	}
	
	public static interface ArrayIterator {
		public int iterate(int x, int y);
	}
	
}
