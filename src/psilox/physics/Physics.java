package psilox.physics;

import static java.lang.Math.*;

import java.awt.geom.Line2D;

import psilox.math.Vec; 

public class Physics {

	public static boolean intersecting(AABB a, AABB b) {
		return (abs(a.c.x - b.c.x) <= (a.r.x + b.r.x)) && (abs(a.c.y - b.c.y) <= (a.r.y + b.r.y));
	}
	
	public static boolean inside(Vec p, AABB a) {
		return (abs(p.x - a.c.x) <= a.r.x) && (abs(p.y - a.c.y) <= a.r.y);
	}
	
	public static boolean linesIntersect(Vec o1, Vec e1, Vec o2, Vec e2) {
		return Line2D.linesIntersect(o1.x, o1.y, e1.x, e1.y, o2.x, o2.y, e2.x, e2.y);
	}
	
	public static boolean cast(Vec o, Vec e, AABB box) {
		if(inside(o, box) || inside(e, box)) return true;
		
		Vec bl = new Vec(box.c.x - box.r.x, box.c.y - box.r.y);
		Vec s = new Vec(box.r.x * 2, box.r.y * 2);
			
		if(Line2D.linesIntersect(o.x, o.y, e.x, e.y, bl.x, bl.y, bl.x + s.x, bl.y + s.y)) return true;
			
		if(Line2D.linesIntersect(o.x, o.y, e.x, e.y, bl.x, bl.y + s.y, bl.x + s.x, bl.y)) return true;
		
		return false;
	}
	
}
