package psilox.demo.asteroids;

import static psilox.graphics.Draw.*;
import static psilox.input.Input.*;

import org.lwjgl.opengl.GL11;

import psilox.core.Node;
import psilox.graphics.Color;
import psilox.math.Random;
import psilox.math.Vec;

public class Player extends Node {
	
	private static final Vec ACCELERATION = new Vec(0, .1f);
	private static final float ROTATION = 3;
	
	private static final Vec[] SHIP_VERTS = { new Vec(0, 20), new Vec(-10, -20), new Vec(0, -10), new Vec(10, -20) };
	private static final Vec[] FLAME_VERTS = { new Vec(0, -10), new Vec(-5, -15), new Vec(5, -15) };
	private static final Color[] FLAME_COLORS = { Color.BLUE, Color.YELLOW, Color.RED };
	
	private Vec velocity = new Vec(0);
	private boolean accelerating;
	
	public void update() {
		if(accelerating = keyDown(W))  {
			velocity = velocity.sum(ACCELERATION.rot(rtn())).clm(10);
		}
		
		if (keyDown(A))
			transform.rotate(ROTATION);
		else if(keyDown(D)) 
			transform.rotate(-ROTATION);
		
		Vec pos = pos();
		pos.x = (pos.x + velocity.x + ((pos.x < 0) ? viewSize().x : 0)) % viewSize().x;
		pos.y = (pos.y + velocity.y + ((pos.y < 0) ? viewSize().y : 0)) % viewSize().y;
		
		if(keyDown(SPACE) && psilox().ticks() % 10 == 0) {
			Bullet b = new Bullet();
			b.transform().setPosition(pos().sum(SHIP_VERTS[0].rot(rtn())));
			b.transform().setRotation(rtn());
			getParent().addChild(b);
		}
	}
	
	public void render() {
		outline(Color.WHITE, SHIP_VERTS);
		if(accelerating) {
			Vec off = new Vec(Random.intVal(-3, 4), -25 + Random.intVal(-5, 6));
			immediate(GL11.GL_TRIANGLES, FLAME_COLORS, new Vec[] { FLAME_VERTS[0], FLAME_VERTS[1], off });
			immediate(GL11.GL_TRIANGLES, FLAME_COLORS, new Vec[] { FLAME_VERTS[0], FLAME_VERTS[2], off });
		}
	}
	
}
