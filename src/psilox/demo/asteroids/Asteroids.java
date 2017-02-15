package psilox.demo.asteroids;

import static psilox.graphics.Draw.immediate;
import static psilox.graphics.Draw.outline;
import static psilox.input.Input.A;
import static psilox.input.Input.D;
import static psilox.input.Input.SPACE;
import static psilox.input.Input.W;
import static psilox.input.Input.keyDown;

import org.lwjgl.opengl.GL11;

import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.graphics.Shader;
import psilox.math.Random;
import psilox.math.Transform;
import psilox.math.Vec;
import psilox.node.Node;
import psilox.node.Timer;

public class Asteroids extends Node {

	private Sky sky;
	private Player player;
	
	public void added() {
		addChild(sky = new Sky());
		sky.transform().translate(new Vec(0, 0, -1));
		
		addChild(player = new Player());
		player.transform().translate(viewSize().scl(.5f));
		
		addChild(new Timer("spawner", 6, false, () -> { addChild(new Asteroid()); } ).start());
	}
	
	public void update() {
		for(Node a : getChildren(Asteroid.class)) {
			if(a.pos().dst(player.pos()) < 40) {
				removeChild(a);
				player.transform().setPosition(viewSize().scl(.5f));
				continue;
			}
			
			for(Node b : getChildren(Bullet.class)) {
				if(a.pos().dst(b.pos()) < 40) {
					removeChild(a);
					removeChild(b);
					break;
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new Psilox(new Config("Asteroids", 1280, 720, false)).start(new Asteroids());
	}
	
}

class Sky extends Node {
	
	private Shader sky;
	
	public void added() {
		sky = new Shader("shaders/sky.shd");
		sky.enable();
		sky.setUniform4f("color", new Color(2, 2, 10));
		sky.setUniform1f("threshold", .97f);
		sky.disable();
	}
	
	public void render() {
		sky.enable();
		sky.setUniform1f("time", psilox().ticks() / 40.0f);
		Draw.quad(Color.WHITE, Vec.ZERO, viewSize());
		sky.disable();
	}
	
}

class Player extends Node {
	
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
			b.setTransform(new Transform(null, pos().sum(SHIP_VERTS[0].rot(rtn())), rtn()));
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

class Bullet extends Node {
	
	private Vec velocity;
	
	public void added() {
		velocity = new Vec(0, 10).rot(rtn());
	}
	
	public void update() {
		transform.translate(velocity);
		
		if(!pos().btn(Vec.ZERO, viewSize())) {
			freeSelf();
		}
	}
	
	public void render() {
		Draw.line(Color.RED, Vec.ZERO, new Vec(0, 5));
	}
	
}

class Asteroid extends Node {
	
	private Vec velocity;
	private float spin;
	
	public void added() {
		velocity = new Vec(Random.intVal(-2, 2), Random.intVal(-2, 2));
		transform.setPosition(viewSize().pro(new Vec(Random.floatVal(1), Random.floatVal(1))));
		spin = Random.floatVal(-5, 5);
	}
	
	public void update() {
		transform.translate(velocity);
		transform.rotate(spin);
		
		Vec pos = pos();
		pos.x = (pos.x + velocity.x + ((pos.x < 0) ? viewSize().x : 0)) % viewSize().x;
		pos.y = (pos.y + velocity.y + ((pos.y < 0) ? viewSize().y : 0)) % viewSize().y;
	}
	
	public void render() {
		Draw.ellipse(Color.BROWN, Vec.ZERO, 40, 10);
	}
	
}