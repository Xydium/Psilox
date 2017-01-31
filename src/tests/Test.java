package tests;

import com.xydium.psilox.core.Psilox;
import com.xydium.psilox.core.PsiloxConfig;
import com.xydium.psilox.input.Key;
import com.xydium.psilox.math.Vec;
import com.xydium.psilox.node.Node;
import com.xydium.psilox.rendering.Color;
import com.xydium.psilox.rendering.Primitives;

public class Test extends Node { 
	
	private static final int d = 500;
	
	private int a = 0, b = d;
	private Color color;
	
	public Test(String tag) {
		super(tag);
	}
	
	public void render() {
		for(int i = 0; i < 10; i++) {
			a++; b++;
			color = new Color((float) Math.sin(a * Math.PI * 2 / 500.0), (float) Math.cos(b * Math.PI * 2 / 500.0), 1);
			draw().fixedFunction(Primitives.LINE, color, 
					genPoint(a), genPoint(b)
					//genPoint(a + d), genPoint(b + d),
					//genPoint(a + d + d), genPoint(b + d + d),
					//genPoint(a + d + d + d), genPoint(b + d + d + d)
			);
		}
	}
	
	private Vec genPoint(int v) {
		if(v < 0) v += d * 4;
		v %= d * 4;
		int i = v % d;
		switch(v / d) {
		case 0:
			return new Vec(0, i);
		case 1:
			return new Vec(i, d);
		case 2:
			return new Vec(d, d - i);
		case 3:
			return new Vec(d - i, 0);
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
