import psilox.core.Node;
import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.math.Random;
import psilox.math.Vec;

public class Asteroid extends Node {

	private Vec velocity;
	private float spin;
	
	public void added() {
		velocity = new Vec(Random.floatVal(-2, 2), Random.floatVal(-2, 2));
		spin = Random.floatVal(-3, 3);
		transform.translate(new Vec(0, 0, 0));
	}
	
	public void update() {
		Vec pos = pos();
		pos.x = (pos.x + velocity.x + ((pos.x < 0) ? viewSize().x : 0)) % viewSize().x;
		pos.y = (pos.y + velocity.y + ((pos.y < 0) ? viewSize().y : 0)) % viewSize().y;
		transform.rotate(spin);
		
		for(Node n : getParent().getChildList()) {
			if(n instanceof Bullet) {
				if(n.pos().dst(pos()) < 40) {
					Asteroid a = new Asteroid();
					a.transform().setPosition(new Vec(Random.floatVal(viewSize().x), Random.floatVal(viewSize().y)));
					getParent().addChild(a);
					getParent().removeChild(n);
					getParent().removeChild(this);
				}
			}
		}
	}
	
	public void render() {
		Draw.ellipse(Color.LIGHT_GRAY, Vec.ZERO, 40, 10);
	}
	
}
