package psilox.demo.game;

import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.input.Input;
import psilox.math.Mathf;
import psilox.math.Vec;
import psilox.node.Node;

public class Player extends MapObject {

	private float speed;
	public final Vec mapPosition;
	
	public Player(Game game, TileMap map, Node entities) {
		super(game, map, entities);
		this.mapPosition = new Vec(0);
	}
	
	public void update() {
		if(Input.keyDown(Input.W)) {
			speed += 0.2;
		} else if(Input.keyDown(Input.S)) {
			speed -= 0.2;
		}
		speed = Mathf.clm(speed, -4, 10);
		
		if(Input.keyTap(Input.D)) {
			Tile right = map.at(mapPosition, Vec.RIGHT.rot(rotation - 90));
			if(right.passable) {
				rotation -= 90;
				if(rotation < 0) rotation += 360;
				centerInDirection();
			}
		} else if(Input.keyTap(Input.A)) {
			Tile left = map.at(mapPosition, Vec.LEFT.rot(rotation - 90));
			if(left.passable) {
				rotation += 90;
				centerInDirection();
			}
		}
		
		Vec nextPos = mapPosition.sum(Vec.angMag(rotation, speed));
		if(map.at(nextPos, Vec.ZERO).passable) {
			mapPosition.set(nextPos);
		}
	}
	
	public void render() {
		//Right Check
		Draw.line(Color.WHITE, Vec.DOWN.scl(20), Vec.DOWN.scl(30));
		//Left Check
		Draw.line(Color.BLACK, Vec.UP.scl(20), Vec.UP.scl(30));
		Draw.ellipsef(Color.ORANGE, Vec.ZERO, 20, 20);
	}
	
	private void centerInDirection() {
		if(Mathf.btn(rotation % 180, 80, 100)) {
			mapPosition.x = (int) (mapPosition.x / Tile.SIZE.x) * Tile.SIZE.x + Tile.SIZE.x / 2;  
		} else {
			mapPosition.y = (int) (mapPosition.y / Tile.SIZE.y) * Tile.SIZE.y + Tile.SIZE.y / 2;
		}
	}
	
}
