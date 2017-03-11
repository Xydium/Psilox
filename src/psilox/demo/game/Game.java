package psilox.demo.game;

import psilox.input.Input;
import psilox.math.Vec;
import psilox.node.Node;

public class Game extends Node {

	private Level level;
	private Tile[][] map;
	
	public Game(Level level) {
		this.level = level;
	}
	
	public void enteredTree() {
		Tile.loadAllTiles();
		this.map = level.loadMap();
	}
	
	public void update() {
		if(Input.keyDown(Input.LEFT)) {
			position.add(new Vec(5, 0));
		} else if(Input.keyDown(Input.RIGHT)) {
			position.add(new Vec(-5, 0));
		}
		if(Input.keyDown(Input.UP)) {
			position.add(new Vec(0, -5));
		} else if(Input.keyDown(Input.DOWN)) {
			position.add(new Vec(0, 5));
		}
	}
	
	public void render() {
		Vec viewSize = viewSize();
		int startX = (int) ((int) (-position.x - Tile.SIZE.x) / Tile.SIZE.x);
		int endX = startX + 18;
		int startY = (int) ((int) (-position.y + viewSize.y + Tile.SIZE.y) / Tile.SIZE.y);
		int endY = startY - 11;
		Vec coordinate = new Vec(0);
		for(int x = startX; x < endX; x++) {
			for(int y = endY; y < startY; y++) {
				coordinate.set(x, y);
				try {
					map[x][y].render(coordinate);
				} catch(Exception e) {}
			}
		}
	}
	
}
