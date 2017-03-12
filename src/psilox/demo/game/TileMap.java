package psilox.demo.game;

import psilox.math.Vec;
import psilox.node.Node;

public class TileMap extends Node {
	
	private Tile[][] map;
	
	public TileMap(Tile[][] map) {
		this.map = map;
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
	
	public Tile at(Vec position, Vec offset) {
		Vec coord = position.quo(Tile.SIZE).sum(offset);
		return map[(int) coord.x][(int) coord.y];
	}
	
}
