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
		a = new Transform(new Vec2f((float) Psilox.windowWidth() / 2, (float) Psilox.windowHeight() / 2));
	}
	
	public void update() {
		if(Input.keyDown(Key.SPACE)) {
			a.rotate(1);
		}
		if(Input.keyDown(Key.LEFT)) {
			a.rescale(new Vec2f(0.1f, 0f));
		}
		if(Input.keyDown(Key.RIGHT)) {
			a.rescale(new Vec2f(-0.1f, 0f));
		}
		if(Input.keyDown(Key.UP)) {
			a.rescale(new Vec2f(0f, 0.1f));
		}
		if(Input.keyDown(Key.DOWN)) {
			a.rescale(new Vec2f(0f, -0.1f));
		}
	}

	public void render() {
		Draw.texture(tex, a);
	}
	
	public static void main(String[] args) {
		Psilox.start();
	}
	
}
