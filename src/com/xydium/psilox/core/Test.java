package com.xydium.psilox.core;

import com.xydium.psilox.math.Vec3;
import com.xydium.psilox.node.Node;
import com.xydium.psilox.rendering.Color;
import com.xydium.psilox.rendering.Primitives;

public class Test extends Node { 
	
	private static final int d = 512;
	
	private int a = 0, b = d;
	private Color color;
	
	public Test(String tag) {
		super(tag);
		setUpdating(true);
	}
	
	public void render() {
		for(int i = 0; i < 10; i++) {
			a++; b++;
			color = new Color((float) Math.sin(a / 100.0), (float) Math.cos(b / 100.0), 1);
			draw().fixedFunction(Primitives.LINE, color, 
					genPoint(a), genPoint(b),
					genPoint(a + d), genPoint(b + d),
					genPoint(a + d + d), genPoint(b + d + d),
					genPoint(a + d + d + d), genPoint(b + d + d + d)
			);
		}
	}
	
	private Vec3 genPoint(int v) {
		v %= d * 4;
		int i = v % d;
		switch(v / d) {
		case 0:
			return new Vec3(0, i);
		case 1:
			return new Vec3(i, d);
		case 2:
			return new Vec3(d, d - i);
		case 3:
			return new Vec3(d - i, 0);
		default:
			return null;
		}
	}
	
	public static void main(String[] args) {
		PsiloxConfig cfg = new PsiloxConfig();
		cfg.title = "Primrose";
		cfg.width = d;
		cfg.height = d;
		cfg.fps = 60;
		cfg.clearscreen = false;
		cfg.doubleBuffer = false;
		Psilox.createRuntime(cfg).start(new Test("Main"));
	}
	
}
