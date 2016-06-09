package com.xydium.core;

import java.awt.Color;
import java.awt.Font;

import com.xydium.rendering.Draw;

public class Test {

	private static int x;
	private static String l1, l2;
	
	public static void start() {
		Draw.addDrawingLayer(l1 = "a");
		Draw.addDrawingLayer(l2 = "b");
		Draw.addDrawingLayer("text");
	}
	
	public static void update() {
		x+=2;
		if(x >= Psilox.windowWidth() + 50) Psilox.stop();
	}
	
	public static void render() {
		if(Psilox.getTickNumber() % 2 == 0) {
			String temp = l2;
			l2 = l1;
			l1 = temp;
		}
		Draw.setDrawingLayer(l1);
		Draw.fillCenteredRect(x, 100, 100, 100, new Color(255, 255, 0));
		Draw.setDrawingLayer(l2);
		Draw.fillCenteredRect(Psilox.windowWidth() - x, 100, 100, 100, new Color(255, 0, 255));
		Draw.setDrawingLayer("text");
		Draw.fillRect(640, 360, 1, 1, new Color(255, 255, 255));
		Draw.centeredText("This Is Centered", 640, 360, new Color(25, 50, 75), new Font("Verdana", Font.PLAIN, 36));
	}
	
	public static void main(String[] args) {
		Psilox.start();
	}
	
}
