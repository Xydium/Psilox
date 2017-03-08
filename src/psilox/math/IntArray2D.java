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
	
	public IntArray2D(int width, int height, int[] array) {
		this.width = width;
		this.height = height;
		this.array = array;
	}
	
	public IntArray2D(Vec dim) {
		this((int) dim.x, (int) dim.y);
	}
	
	public int get(int i) {
		return array[i];
	}
	
	public int get(int x, int y) {
		return array[y * width + x];
	}
	
	public int getSafe(int x, int y, int d) {
		if(x < 0 || x >= width || y < 0 || y >= height) {
			return d;
		} else {
			return get(x, y);
		}
	}
	
	public void set(int i, int v) {
		array[i] = v;
	}
	
	public void set(int x, int y, int v) {
		array[y * width + x] = v;
	}
	
	public int[][] getArea(int x, int y, int w, int h, int d) {
		int[][] area = new int[w][h];
		for(int xx = x; xx < x + w; xx++) {
			for(int yy = y; yy < y + h; yy++) {
				area[xx - x][yy - y] = getSafe(xx, yy, d);
			}
		}
		return area;
	}
	
	public void iterate(MatrixIterator it) {
		int i = 0;
		int x = 0;
		for(int y = 0; y < height; y++) {
			for(x = 0; x < width; x++) {
				array[i] = it.iterate(x, y, array[i]);
				i++;
			}
		}
	}
	
	public void iterate(LinearIterator it) {
		for(int i = 0; i < array.length; i++) {
			array[i] = it.iterate(i, array[i]);
		}
	}
	
	public static interface MatrixIterator {
		public int iterate(int x, int y, int v);
	}
	
	public static interface LinearIterator {
		public int iterate(int i, int v);
	}
	
}
