package com.xydium.psilox.rendering;

import com.xydium.psilox.math.Vec3;

public class Primitives {

	public static final Vec3[] RECT = {
			new Vec3(0, 0),
			new Vec3(1, 0),
			new Vec3(1, 1),
			new Vec3(0, 1)
	};
		
	public static final Vec3[] C_RECT = {
			new Vec3(-.5f, -.5f),
			new Vec3(.5f, -.5f),
			new Vec3(.5f, .5f),
			new Vec3(-.5f, .5f),
	};
	
	public static final Vec3[] UTRI = {
		new Vec3(0, 0),
		new Vec3(1, 0),
		new Vec3(.5f, 1)
	};
	
	public static final Vec3[] EQTRI = {
		new Vec3(0, 0),
		new Vec3(1, 0),
		new Vec3(.5f, .866f)
	};
	
	public static final Vec3[] C_UTRI = {
		new Vec3(0, .5f),
		new Vec3(-.5f, -.5f),
		new Vec3(.5f, -.5f)
	};
	
	public static final Vec3[] C_EQTRI = {
		new Vec3(0, .433f),
		new Vec3(-.5f, -.433f),
		new Vec3(.5f, -.433f)
	};
	
}
