import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.math.Vec;
import psilox.node.Node;

public class Test extends Node {

	private Node rect;
	
	public void enteredTree() {
		rect = new Node() {
			public void render() {
				Draw.quad(Color.RED, Vec.ZERO, new Vec(100));
			}
			
			public Vec getDimensions() { 
				return new Vec(100);
			}
		};
		
		rect.position.x = 100;
		rect.position.y = 100;
		
		addChild(rect);
	}
	
	public void update() {
		rect.setAnchor(Anchor.values()[(int) Psilox.ticks() % 9]);
	}
	
	public void render() {
		Draw.cquad(Color.GREEN, new Vec(100), new Vec(10));
	}
	
	public static void main(String[] args) {
		Config c = new Config("Test", 1280, 720, false);
		c.updateRate = 2;
		Psilox.start(c, new Test());
	}
	
}
