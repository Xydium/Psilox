package com.xydium.core;

import java.awt.Color;
import java.awt.Font;

import com.xydium.rendering.Draw;

public class Test {
	
	public static void start() {
		Draw.addDrawingLayer("a");
		Draw.addDrawingLayer("b");
		Draw.addDrawingLayer("text");
	}
	
	public static void update() {
		
	}
	
	public static void render() {
		Draw.setDrawingLayer("b");
		Draw.fillCenteredRect(63, 36, 4, 4, new Color(255, 255, 0));
		Draw.setDrawingLayer("a");
		Draw.fillCenteredRect(65, 36, 4, 4, new Color(255, 0, 255));
		Draw.setDrawingLayer("text");
		Draw.centeredText("It's Shat Boi", 128, 72, new Color(255, 50, 75), new Font("Verdana", Font.PLAIN, 12));
	}
	
	public static void main(String[] args) {
		Psilox.start();
		Psilox.addExitProtocol(() -> {
			System.out.println("Goodbye");
		});
		Psilox.addRuntimeProtocol(() -> {
			
		}, 60);
	}
	
}
