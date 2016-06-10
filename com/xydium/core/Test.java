package com.xydium.core;

import java.awt.Color;
import java.awt.Font;

import com.xydium.geometry.Vec2;
import com.xydium.geometry.Vec2i;
import com.xydium.rendering.Draw;
import com.xydium.utility.Input;
import com.xydium.utility.InputMap;
import com.xydium.utility.Key;

public class Test extends Scene {
	
	private Vec2<Integer> pos1, pos2;
	private InputMap player1, player2;
	
	public void load() {
		pos1 = new Vec2i(Psilox.windowWidth() / 4, Psilox.windowHeight() / 4);
		pos2 = new Vec2i(Psilox.windowWidth() / 4 * 3, Psilox.windowHeight() / 4 * 3);
		player1 = new InputMap("player1.imap");
		player2 = new InputMap("player2.imap");
	}
	
	public void activate() {
		Draw.addDrawingLayer("background");
		Draw.addDrawingLayer("players");
		Draw.addDrawingLayer("text");
	}
	
	public void update() {
		if(player1.actionDown("move_up")) {
			pos1 = pos1.add(new Vec2i(0, -5));
		} else if(player1.actionDown("move_down")) {
			pos1 = pos1.add(new Vec2i(0, 5));
		}
		if(player1.actionDown("move_left")) {
			pos1 = pos1.add(new Vec2i(-5, 0));
		} else if(player1.actionDown("move_right")) {
			pos1 = pos1.add(new Vec2i(5, 0));
		}
		
		if(player2.actionDown("move_up")) {
			pos2 = pos2.add(new Vec2i(0, -5));
		} else if(player2.actionDown("move_down")) {
			pos2 = pos2.add(new Vec2i(0, 5));
		}
		if(player2.actionDown("move_left")) {
			pos2 = pos2.add(new Vec2i(-5, 0));
		} else if(player2.actionDown("move_right")) {
			pos2 = pos2.add(new Vec2i(5, 0));
		}
		
		if(Input.keyTap(Key.ESCAPE)) {
			Psilox.setScene(new Test2());
		}
	}
	
	public void render() {
		Draw.setDrawingLayer("background");
		Draw.fillRect(new Vec2i(0), new Vec2i(Psilox.windowWidth(), Psilox.windowHeight()), new Color(25, 25, 0));
		Draw.setDrawingLayer("players");
		Draw.fillCenteredRect(pos1, new Vec2i(32, 32), Color.MAGENTA);
		Draw.fillCenteredRect(pos2, new Vec2i(32, 32), Color.YELLOW);
		Draw.setDrawingLayer("text");
		Draw.centeredText("Psilox Input Map Demo", new Vec2i(Psilox.windowWidth() / 2, 32), Color.WHITE, new Font("Verdana", Font.PLAIN, 32));
	}
	
	public static void main(String[] args) {
		Psilox.start();
	}
	
}