package psilox.demo.game;

import psilox.node.Node;

public class MapObject extends Node {

	public final Game game;
	public final TileMap map;
	public final Node entities;
	
	public MapObject(Game game, TileMap map, Node entities) {
		this.game = game;
		this.map = map;
		this.entities = entities;
	}
	
}
