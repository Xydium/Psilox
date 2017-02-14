package psilox.demo.asteroids;

import psilox.core.Node;
import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.math.Vec;

public class Bullet extends Node {

	private Vec velocity;
	
	public void added() {
		velocity = new Vec(0, 10).rot(rtn());
		transform.translate(new Vec(0, 0, 0));
	}
	
	public void update() {
		transform.translate(velocity);
		
		if(!pos().btn(Vec.ZERO, viewSize())) {
			getParent().removeChild(this);
		}
	}
	
	public void render() {
		Draw.line(Color.RED, Vec.ZERO, new Vec(0, 5));
	}
	
}
