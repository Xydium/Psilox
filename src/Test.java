import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.math.Vec;
import psilox.node.Node;


public class Test extends Node {

	public void enteredTree() {
		Node a = new Node() {
			public void render() {
				Draw.cquad(Color.RED, Vec.ZERO, new Vec(50));
			}
		};
		
		Node b = new Node() {
			public void render() {
				Draw.cquad(Color.RED, Vec.ZERO, new Vec(50));
			}
		};
		
		a.position.set(250, 250);
		b.position.set(50, 50);
		
		addChild(a);
		a.addChild(b);
	}
	
	public void update() {
		Node a = getChild(0);
		Node b = a.getChild(0);
		
		a.rotation += 1;
		b.rotation += 2;
	}
	
	public static void main(String[] args) {
		Psilox.start(new Config("Test", 1280, 720, false), new Test());
	}
	
}
