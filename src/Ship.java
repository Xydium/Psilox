import static psilox.graphics.Draw.*;
import static psilox.input.Input.*;

import org.lwjgl.opengl.GL11;

import psilox.core.Config;
import psilox.core.Node;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.math.Random;
import psilox.math.Vec;

public class Ship extends Node {

	private Vec velocity = new Vec(0);
	private boolean accelerating;
	
	public void added() {
		getRoot().addChild(new Sky());
		Asteroid a = new Asteroid();
		a.transform().setPosition(new Vec(50));
		getRoot().addChild(a);
		transform.translate(viewSize().scl(.5f));
	}
	
	public void update() {
		if(accelerating = keyDown(W)) velocity = velocity.sum(new Vec(0, .1f).rot(rtn())).clm(10);
		if(keyDown(A)) transform.rotate(3); else
		if(keyDown(D)) transform.rotate(-3);
		
		Vec pos = pos();
		pos.x = (pos.x + velocity.x + ((pos.x < 0) ? viewSize().x : 0)) % viewSize().x;
		pos.y = (pos.y + velocity.y + ((pos.y < 0) ? viewSize().y : 0)) % viewSize().y;
		
		if(keyDown(SPACE) && psilox().ticks() % 10 == 0) {
			Bullet b = new Bullet();
			b.transform().setPosition(pos().sum(new Vec(0, 20).rot(rtn())));
			b.transform().setRotation(rtn());
			getRoot().addChild(b);
		}
		
		for(Node n : getParent().getChildList()) {
			if(n instanceof Asteroid) {
				if(n.pos().dst(pos()) < 30) {
					transform.setPosition(viewSize().scl(.5f));
				}
			}
		}
	}
	
	public void render() {
		outline(Color.WHITE, new Vec(0, 20), new Vec(-10, -20), new Vec(0, -10), new Vec(10, -20));
		if(accelerating) {
			int hOff = Random.intVal(-3, 4), vOff = Random.intVal(-5, 6);
			Vec a = new Vec(0, -10), b = new Vec(-5, -15), c = new Vec(5, -15), d = new Vec(hOff, -25 + vOff);   
			immediate(GL11.GL_TRIANGLES, new Color[] {Color.BLUE, Color.YELLOW, Color.RED}, new Vec[] {a, b, d});
			immediate(GL11.GL_TRIANGLES, new Color[] {Color.BLUE, Color.YELLOW, Color.RED}, new Vec[] {a, c, d});
		}
	}
	
	public static void main(String[] args) { 
		Config c = new Config();
		c.width = 1280;
		c.height = 720;
		new Psilox(c).start(new Ship()); 
	}
	
}
