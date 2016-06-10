package com.xydium.core;

import com.xydium.geometry.Transform;
import com.xydium.geometry.Vec2f;
import com.xydium.rendering.Draw;
import com.xydium.resources.Texture;
import com.xydium.utility.Input;
import com.xydium.utility.Key;

public class Test2 extends Scene {
	
	private Texture tex;
	private Transform a;
	
	public void load() {
		tex = new Texture("011.png");
		a = new Transform(new Vec2f(50f, 50f));
	}
	
	public void update() {
		if(Input.keyTap(Key.ESCAPE)) {
			Psilox.setScene(new Test());
		}
		if(Input.keyDown(Key.SPACE)) {
			a.rotate(10);
		}
	}

	public void render() {
		Draw.texture(tex, a);
	}
	
}
