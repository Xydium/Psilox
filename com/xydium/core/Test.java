package com.xydium.core;

import java.awt.Color;
import java.awt.Font;

import com.xydium.geometry.TrigFunction;
import com.xydium.geometry.TrigFunction.TrigFunctionType;
import com.xydium.geometry.Vec2f;
import com.xydium.geometry.Vec2i;
import com.xydium.rendering.Draw;

public class Test extends Scene {
	
	private Color color;
	private Font font;
	private TrigFunction sin, sin2;
	private Vec2i pos;
	
	public void load() {
		color = new Color(255, 25, 50);
		font = new Font("Verdana", Font.PLAIN, 12);
		sin = new TrigFunction(1, 10, TrigFunctionType.SIN);
		sin2 = new TrigFunction(20, 1, TrigFunctionType.SIN);
		pos = new Vec2i(Psilox.windowWidth() / 2, Psilox.windowHeight() / 2);
	}
	
	public void update() {
		sin.setAmplitude(sin2.next() + sin2.getAmplitude());
		pos.setY((int) (sin.next() + Psilox.windowHeight() / 2));
	}
	
	public void render() {
		Draw.centeredText("Welcome to Psilox", pos, color, font);
	}
	
	public static void main(String[] args) {
		Psilox.start();
	}
	
}