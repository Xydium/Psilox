package com.xydium.psilox.core;

import com.xydium.psilox.math.Vec3;
import com.xydium.psilox.node.Node;
import com.xydium.psilox.node.Rect;
import com.xydium.psilox.rendering.Color;
import com.xydium.psilox.rendering.Primitives;

public class Test extends Node {

	private Rect r;
	
	public Test(String tag) {
		super(tag);
		setUpdating(true);
	}
	
	public void added() {
		transform.translate(new Vec3(100, 360));
		
		r = new Rect("Shape", new Vec3(75, 25), true, new Color(1, 0.2f, 0.8f)); 
		r.getTransform().translate(new Vec3(100, 0));
		addChild(r);
	}
	
	public void update() {
		r.getTransform().translate(Vec3.X_UNIT.sca(2));
		transform.translate(Vec3.X_UNIT);
	}
	
	public void render() {
		draw().fixedFunction(
				Primitives.LINE, 
				new Vec3[] { 
						Vec3.ZERO, 
						r.getTransform().position()
				},  
				new Color(1, 1, 1)
		);
	}
	
	public static void main(String[] args) {
		PsiloxConfig cfg = new PsiloxConfig();
		cfg.width = 1280;
		cfg.height = 720;
		Psilox.createRuntime(cfg).start(new Test("Main"));
	}
	
}
