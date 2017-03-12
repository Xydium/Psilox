package psilox.demo.game;

import psilox.node.Node;

public class Game extends Node {
	
	private Level level;
	private TileMap map;
	private Node entities;
	private Player player;
	
	public Game(Level level) {
		this.level = level;
	}
	
	public void enteredTree() {
		Tile.loadAllTiles();
		map = new TileMap(level.loadMap());
		entities = new Node();
		
		player = new Player(this, map, entities);
		player.position.set(viewSize().quo(2));
		player.mapPosition.set(level.spawnPoint);
		
		entities.position.z = 1;
		player.position.z = 2;
		
		map.addChild(entities);
		addChildren(map, player);
	}
	
	public void render() {
		map.position.set(player.mapPosition.scl(-1).sum(viewSize().quo(2)));
	}
	
}
