package com.xydium.psilox.core;

import com.xydium.psilox.math.Random;
import com.xydium.psilox.math.Vec3;
import com.xydium.psilox.node.Node;
import com.xydium.psilox.rendering.Color;
import com.xydium.psilox.rendering.Primitives;
import com.xydium.psilox.utilities.Time;

public class Test extends Node { 
	
	public Test() {
		super("Test" + Random.intVal());
		setUpdating(true);
	}
	
	public void render() {
		for(int i = 0; i < 500; i+= 10) {
			draw().fixedFunction(
					Primitives.LINE,
					new Vec3[] {
							Vec3.X_UNIT.sca(i),
							Vec3.Y_UNIT.sca(500 - i)
					},
					new Color(255, 0, 255)
			);
		}
	}
	
	public static void main(String[] args) {
		PsiloxConfig cfg = new PsiloxConfig();
		cfg.width = 500;
		cfg.height = 500;
		cfg.fps = 60;
		cfg.clearscreen = true;
		Psilox.createRuntime(cfg).start(Test.class);
	}
	
}
