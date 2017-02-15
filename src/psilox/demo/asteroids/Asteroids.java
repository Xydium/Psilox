package psilox.demo.asteroids;

import static psilox.graphics.Draw.*;
import static psilox.input.Input.*;

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
		addChildren(sky = new Sky(), player = new Player());
		sky.transform().translate(new Vec(0, 0, -1));
		player.transform().translate(viewSize().scl(.5f));
		addChild(new Timer("spawner", 4, false, () -> { addChild(new Asteroid(Asteroid.FULL)); } ).start());
	}
	
	public void update() {
		for(Node a : getChildren(Asteroid.class)) {
			if(a.pos().dst(player.pos()) < ((Asteroid) a).getRadius() + 10) {
				removeChild(a);
				addChild(new Explosion(player.pos(), player.rtn()));
				player.transform().setPosition(viewSize().scl(.5f));
				continue;
			}
			
			for(Node b : getChildren(Bullet.class)) {
				if(a.pos().dst(b.pos()) < ((Asteroid) a).getRadius() + 10) {
					((Asteroid) a).split();
					addChild(new Explosion(a.pos(), a.rtn()));
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
		sky.setUniform1f("time", psilox().ticks() / 10f);
		Draw.quad(Color.WHITE, Vec.ZERO, viewSize());
		sky.disable();
	}
	
}

class Player extends Node {
	
	private static final Vec ACCELERATION = new Vec(0, .1f);
	private static final float ROTATION = 3;
	
	private static final Vec[] SHIP_VERTS = { new Vec(0, 20), new Vec(-10, -20), Vec.ZERO, new Vec(10, -20) };
	private static final Vec[] FLAME_VERTS = { SHIP_VERTS[2], SHIP_VERTS[1], SHIP_VERTS[3] };
	private static final Color[] FLAME_COLORS = { Color.BLUE.aAdj(.5f), Color.YELLOW.aAdj(.7f), Color.RED };
	
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
			Vec off = new Vec(Random.intVal(-3, 4), -40 + Random.intVal(-5, 6));
			immediate(4, FLAME_COLORS, new Vec[] { FLAME_VERTS[0], FLAME_VERTS[1], off });
			immediate(4, FLAME_COLORS, new Vec[] { FLAME_VERTS[0], FLAME_VERTS[2], off });
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
	
	public static final int FULL = 40;
	public static final int HALF = FULL / 2;
	
	private Vec velocity;
	private float spin;
	private int radius;
	
	public Asteroid(int radius) {
		super();
		this.radius = radius;
	}
	
	public void added() {
		velocity = new Vec(Random.intVal(-2, 2), Random.intVal(-2, 2));
		if(radius == FULL) {
			transform.setPosition(viewSize().pro(new Vec(Random.floatVal(1), Random.floatVal(1))));
		}
		spin = Random.floatVal(-5, 5);
	}
	
	public void update() {
		transform.translate(velocity);
		transform.rotate(spin);
		
		Vec pos = pos();
		pos.x = (pos.x + velocity.x + ((pos.x < 0) ? viewSize().x : 0)) % viewSize().x;
		pos.y = (pos.y + velocity.y + ((pos.y < 0) ? viewSize().y : 0)) % viewSize().y;
	}
	
	public void split() {
		if(radius == FULL) {
			Asteroid a = new Asteroid(HALF);
			Asteroid b = new Asteroid(HALF);
			a.transform.setPosition(pos().sum(new Vec(20, 20)));
			b.transform.setPosition(pos().sum(new Vec(-20, -20)));
			getParent().addChildren(a, b);
		}
	}
	
	public float getRadius() {
		return radius;
	}
	
	public void render() {
		Draw.ellipse(Color.BROWN, Vec.ZERO, radius, 10);
	}
	
}

class Explosion extends Node {
	
	private static final float delta = 40;
	
	private float distanceOut;
	private float distanceIn;
	
	public Explosion(Vec pos, float rot) {
		super();
		transform.setPosition(pos);
		transform.setRotation(rot);
	}
	
	public void update() {
		if(distanceOut < viewSize().x) {
			distanceOut += 1.5f * delta;
		}
		if(distanceIn < viewSize().x) {
			distanceIn += delta;
		} else {
			freeSelf();
		}
	}
	
	public void render() {
		Vec[] points = new Vec[16];
		for(int i = 0; i < points.length; i += 2) {
			points[i] = Vec.angMag(i / 2 * 45, distanceIn);
			points[i + 1] = Vec.angMag(i / 2 * 45, distanceOut);
		}
		Draw.immediate(1, Color.ORANGE.aAdj(.5f), points);
	}
	
}