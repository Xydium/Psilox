package psilox.demo.game;

import psilox.node.Node;
import psilox.node.Sprite;

public class MapObject extends Sprite {

	public final Game game;
	public final TileMap map;
	public final Node entities;
	
	public MapObject(String tex, Game game, TileMap map, Node entities) {
		super(tex);
		this.game = game;
		this.map = map;
		this.entities = entities;
	}
	
}
