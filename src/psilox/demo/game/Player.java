package psilox.demo.game;

import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.input.Input;
import psilox.input.LogicMap;
import psilox.math.Mathf;
import psilox.math.Vec;
import psilox.node.Node;

public class Player extends MapObject {

	public float speed;
	public final Vec mapPosition;
	
	public Player(Game game, TileMap map, Node entities) {
		super("psilox/demo/game/player.png", game, map, entities);
		setDimensions(new Vec(80));
		this.mapPosition = new Vec(0);
	}
	
	public void update() {
		accelerate();
		attemptTurns();
		moveByVelocity();
	}
	
	private void accelerate() {
		if(Input.keyDown(Input.W)) {
			speed += 0.2;
		} else if(Input.keyDown(Input.S)) {
			speed -= 0.2;
		} else if(Input.keyDown(Input.SPACE)) {
			speed += -(speed / 10);
			if(Math.abs(speed) < 0.01) {
				speed = 0;
			}
		}
		speed = Mathf.clm(speed, -4, 10);
	}
	
	private void attemptTurns() {
		float dr = 0;
		if(Input.keyTap(Input.D) && map.at(mapPosition, Vec.RIGHT.rot(rotation - 90)).passable) {
			dr = -90;
		} else if(Input.keyTap(Input.A) && map.at(mapPosition, Vec.LEFT.rot(rotation - 90)).passable) {
			dr = 90;
		}
		rotation += dr; 
		if(rotation < 0) rotation += 360;
		centerInDirection();
	}
	
	private void moveByVelocity() {
		Vec nextPos = mapPosition.sum(Vec.angMag(rotation, speed));
		Vec checkPos = nextPos.sum(Vec.angMag(rotation, speed + Tile.SIZE.x / 2 * Math.signum(speed)));
		if(map.at(checkPos, Vec.ZERO).passable) {
			mapPosition.set(nextPos);
		} else {
			speed = Mathf.clm(speed - .5, 0, 10);
			centerInTile();
		}
	}
	
	private void centerInDirection() {
		if(Mathf.btn(rotation % 180, 80, 100)) {
			mapPosition.x = (int) (mapPosition.x / Tile.SIZE.x) * Tile.SIZE.x + Tile.SIZE.x / 2;  
		} else {
			mapPosition.y = (int) (mapPosition.y / Tile.SIZE.y) * Tile.SIZE.y + Tile.SIZE.y / 2;
		}
	}
	
	private void centerInTile() {
		mapPosition.x = (int) (mapPosition.x / Tile.SIZE.x) * Tile.SIZE.x + Tile.SIZE.x / 2;
		mapPosition.y = (int) (mapPosition.y / Tile.SIZE.y) * Tile.SIZE.y + Tile.SIZE.y / 2;
	}
	
	public void render() {
		//Right Check
		Draw.line(Color.WHITE, Vec.DOWN.scl(20), Vec.DOWN.scl(80));
		//Left Check
		Draw.line(Color.BLACK, Vec.UP.scl(20), Vec.UP.scl(80));
		//Mouse Line
		Draw.line(Input.buttonDown(Input.BUTTON_LEFT) ? Color.YELLOW : Color.RED, Vec.ZERO, relMouse().rot(-rotation));
		//Draw Texture
		Draw.texture(texture, getDimensions().scl(-.5f), getDimensions(), modulate);
	}
	
	private Vec relMouse() {
		return Input.position.dif(viewSize().quo(2));
	}
	
}
