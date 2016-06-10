package com.xydium.core;

import java.awt.Color;
import java.awt.Font;

import com.xydium.geometry.Transform;
import com.xydium.geometry.Vec2f;
import com.xydium.geometry.Vec2i;
import com.xydium.rendering.Draw;
import com.xydium.resources.Texture;
import com.xydium.utility.Input;
import com.xydium.utility.Key;

public class Test2 extends Scene {
	
	private static final Vec2i TEXT_POSITION = new Vec2i(Psilox.windowWidth() / 2, 100);
	
	private Texture tex;
	
	private Transform a, b;
	
	public void load() {
		tex = new Texture("011.png");
		a = new Transform(new Vec2f(50f, 50f));
		a.rotate(45);
		b = new Transform(a, new Vec2f(200f, 200f), new Vec2f(2.0f), 0);
	}
	
	public void activate() {
		
	}
	
	public void deactivate() {
		
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
		Draw.centeredText("Scene Switching Test", TEXT_POSITION, Color.WHITE, new Font("Verdana", Font.PLAIN, 32));
		Draw.texture(tex, b);
	}
	
}
