import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.math.Random;
import psilox.math.Vec;
import psilox.node.Node;

public class Asteroid extends Node {

	private Vec velocity;
	private float spin;
	private boolean destroyed;
	
	public void added() {
		velocity = new Vec(Random.floatVal(-2, 2), Random.floatVal(-2, 2));
		spin = Random.floatVal(-3, 3);
		transform.translate(new Vec(0, 0, 0));
	}
	
	public void update() {
		if(destroyed) {
			getParent().removeChild(this);
		}
		
		Vec pos = pos();
		pos.x = (pos.x + velocity.x + ((pos.x < 0) ? viewSize().x : 0)) % viewSize().x;
		pos.y = (pos.y + velocity.y + ((pos.y < 0) ? viewSize().y : 0)) % viewSize().y;
		transform.rotate(spin);
		
		for(Node n : getParent().getChildList()) {
			if(n instanceof Bullet) {
				if(n.pos().dst(pos()) < 40) {
					destroyed = true;
					Asteroid a = new Asteroid();
					a.transform().setPosition(new Vec(Random.floatVal(viewSize().x), Random.floatVal(viewSize().y)));
					getParent().addChild(a);
					getParent().removeChild(n);
				}
			}
		}
	}
	
	public void render() {
		Draw.ellipse(Color.LIGHT_GRAY, Vec.ZERO, 40, 10);
	}
	
}
