package psilox.physics;

public class AABB {

	public AABBPoint c;
	public AABBPoint r;
	
	public AABB(double x, double y, double width, double height) {
		c = new AABBPoint(x, y);
		r = new AABBPoint(width / 2, height / 2);
	}
	
	class AABBPoint {
		public double x;
		public double y;
		
		public AABBPoint(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
	
}

