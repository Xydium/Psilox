package com.xydium.core;

import java.awt.Color;
import java.awt.Font;

import com.xydium.geometry.Vec2i;
import com.xydium.rendering.Draw;
import com.xydium.utility.Input;
import com.xydium.utility.Key;

public class Test2 extends Scene {
	
	private static final Vec2i TEXT_POSITION = new Vec2i(Psilox.windowWidth() / 2, Psilox.windowHeight() / 2);
	
	public void load() {
		
	}
	
	public void activate() {
		
	}
	
	public void deactivate() {
		
	}
	
	public void update() {
		if(Input.keyTap(Key.ESCAPE)) {
			Psilox.setScene(new Test());
		}
	}
	
	public void render() {
		Draw.centeredText("Scene Switching Test", TEXT_POSITION, Color.WHITE, new Font("Verdana", Font.PLAIN, 32));
	}
	
}
