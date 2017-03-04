package psilox.physics;

import psilox.math.Vec;

import static java.lang.Math.*; 

public class Physics {

	public static boolean intersecting(AABB a, AABB b) {
		return (abs(a.c.x - b.c.x) <= (a.r.x + b.r.x)) && (abs(a.c.y - b.c.y) <= (a.r.y + b.r.y));
	}
	
	public static boolean inside(Vec p, AABB a) {
		return (abs(p.x - a.c.x) <= a.r.x) && (abs(p.y - a.c.y) <= a.r.y);
	}
	
}
