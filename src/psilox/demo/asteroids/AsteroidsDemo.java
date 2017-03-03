package psilox.demo.asteroids;

import static psilox.input.Input.*;

import java.awt.Font;
import java.util.List;

import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.graphics.Shader;
import psilox.graphics.Shape;
import psilox.math.Random;
import psilox.math.Vec;
import psilox.node.Node;
import psilox.node.ui.Button;
import psilox.node.ui.Container;
import psilox.node.ui.Label;
import psilox.node.utility.Interpolator;
import psilox.node.utility.Timer;
import psilox.utils.Pointer.IntPointer;
import psilox.utils.Pointer.StringPointer;

public class AsteroidsDemo {

	public static void main(String[] args) {
		Psilox.start(new Config("Asteroids", 1280, 720, false), new Game());
	}
	
}

class Game extends Node {
	
	private Container UI;
	private Sky sky;
	private Player player;
	private Node projectileList;
	
	private StringPointer sprofile;
	private IntPointer score;
	
	public void enteredTree() {
		UI = new Container(viewSize(), new Vec(10));
		UI.position.z = 2;
		
		sky = new Sky();
		sky.position.z = -1;
		
		projectileList = new Node();
		
		player = new Player();
		player.position.set(viewSize().scl(.5f));
		
		player.bulletList = projectileList;
		Asteroid.asteroidList = projectileList;
		
		score = new IntPointer(0);
		
		Label label = new Label(new Color(155, 155, 255), new Font("Verdana", Font.PLAIN, 32), "Score: %s", score);
		label.setAnchor(Anchor.TL);
		label.position.y += 8;
		UI.topLeft.addChild(label);
		
		Label quit = new Label(new Color(255, 155, 155), new Font("Verdana", Font.BOLD, 48), "Press CTRL-SHIFT-Z or 'Quit' to Quit");
		
		Interpolator quitFade = new Interpolator(v -> {
			quit.setColor(quit.getColor().aAdj(v));
		});
		
		quitFade.setOnEnd(quit::freeSelf);
		quitFade.addKeyFrames(0, 1,   4, 1,   5, 0);
		quitFade.start();
		quit.addChild(quitFade);	
		quit.setAnchor(Anchor.MM);
		UI.center.addChild(quit);
		
		Button b = new Button(new Vec(150, 25), "Quit", () -> { Psilox.stop(); });
		b.setAnchor(Anchor.BR);
		UI.bottomRight.addChild(b);
		
		Timer spawner = new Timer(2, false, () -> { projectileList.addChild(new Asteroid(Asteroid.FULL)); }).start();
		
		Label profile = new Label(Color.WHITE, new Font("Verdana", Font.PLAIN, 18), "Psilox Performance - %s", sprofile = new StringPointer(""));
		UI.bottomLeft.addChild(profile);
		
		addChildren(sky, projectileList, player, UI, spawner);
	}
	
	public void update() {
		if(Psilox.ticks() % 60 == 0) 
			sprofile.set(String.format("Ut: %d, Ft: %d, Ct: %d", 
					(long) (1 / Psilox.updateTime), 
					(long) (1 / Psilox.renderTime), 
					(long) (1 / (Psilox.updateTime + Psilox.renderTime))));
		
		List<Asteroid> asteroids = projectileList.getChildren(Asteroid.class);
		List<Bullet> bullets = projectileList.getChildren(Bullet.class);
		
		for(Asteroid a : asteroids) {
			for(Bullet b : bullets) {
				if(a.position.dst(b.position) < a.getRadius() + 10) {
					a.split();
					b.freeSelf();
					score.add((int) a.getRadius());
					break;
				}
			}
			
			if(a.position.dst(player.position) < a.getRadius() + 10) {
				projectileList.removeAllChildren();
				player.position.set(viewSize().scl(.5f));
				score.set(0);
				addChild(new DeathScreen());
				break;
			}
		}
	}
	
}

class Sky extends Node {
	
	private Shader sky;
	
	public void enteredTree() {
		sky = new Shader("psilox/demo/asteroids/sky.shd");
		sky.enable();
		sky.setUniform4f("color", new Color(2, 2, 10));
		sky.setUniform1f("threshold", .97f);
		sky.disable();
	}
	
	public void render() {
		sky.enable();
		sky.setUniform1f("time", Psilox.ticks() / 15f);
		Draw.quad(Color.WHITE, Vec.ZERO, viewSize());
		sky.disable();
	}
	
}

class Mover extends Node {

	private static final Vec ACCELERATION = new Vec(0, 1);
	
	protected Vec velocity = new Vec(0);
	
	public void accelerate(float amount, float maxSpeed) {
		velocity = velocity.sum(ACCELERATION.rot(rotation).scl(amount)).clm(maxSpeed);
	}
	
	public void move() {
		position.add(velocity);
	}
	
	public boolean onScreen() {
		return position.btn(Vec.ZERO, viewSize());
	}
	
	public void wrapPosition() {
		position.x = (position.x + (position.x < 0 ? viewSize().x : 0)) % viewSize().x;
		position.y = (position.y + (position.y < 0 ? viewSize().y : 0)) % viewSize().y;
	}
	
}

class Player extends Mover {
	
	private static final float ACCELERATION = .1f;
	private static final float MAX_SPEED = 10f;
	private static final float ROTATION = 3.0f;
	
	private static final Vec[] SHIP_VERTS = { new Vec(0, 20), new Vec(-10, -20), Vec.ZERO, new Vec(10, -20) };
	private static final Vec[] FLAME_VERTS = { SHIP_VERTS[2], SHIP_VERTS[1], SHIP_VERTS[3], new Vec(0) };
	private static final Color[] FLAME_COLORS = { Color.BLUE.aAdj(.5f), Color.YELLOW.aAdj(.7f), Color.RED };
	
	private boolean accelerating;
	
	public Node bulletList;
	
	public void update() {
		if(accelerating = keyDown(W)) {
			accelerate(ACCELERATION, MAX_SPEED);
		}
		
		if     (keyDown(A)) rotation += ROTATION;
		else if(keyDown(D)) rotation -= ROTATION;
		
		move();
		wrapPosition();
		
		fire();
	}
	
	private void fire() {
		if(keyDown(SPACE) && Psilox.ticks() % 15 == 0) {
			Bullet b = new Bullet();
			b.position.set(position.sum(SHIP_VERTS[0].sum(new Vec(0, 25)).rot(rotation)));
			b.rotation = rotation;
			bulletList.addChild(b);
		}
	}
	
	public void render() {
		Draw.outline(Color.WHITE, SHIP_VERTS);
		if(accelerating) {
			FLAME_VERTS[3] = new Vec(Random.intVal(-3, 4), -40 + Random.intVal(-5, 6));
			Draw.immediate(4, FLAME_COLORS, new Vec[] { FLAME_VERTS[0], FLAME_VERTS[1], FLAME_VERTS[3] });
			Draw.immediate(4, FLAME_COLORS, new Vec[] { FLAME_VERTS[0], FLAME_VERTS[2], FLAME_VERTS[3] });
		}
	}
	
}

class Bullet extends Mover {
	
	private int lifetime;
	
	public void enteredTree() {
		accelerate(40, 40);
	}
	
	public void update() {
		move();
		
		lifetime++;
		
		if(lifetime * 40 > viewSize().mag()) {
			freeSelf();
		}
	}
	
	public void render() {
		Draw.line(Color.RED, Vec.ZERO, new Vec(0, -(lifetime + 5) * 5));
	}
	
}

class Asteroid extends Mover {
	
	public static final int FULL = 40;
	public static final int HALF = FULL / 2;
	
	private float spin;
	private int radius;
	public static Node asteroidList;
	
	public Asteroid(int radius) {
		super();
		this.radius = radius;
		if(radius == FULL)
			rotation = Random.intVal(360);
	}
	
	public void enteredTree() {
		accelerate(Random.floatVal(1, 3), 3);
		if(radius == FULL) {
			position.set(viewSize().pro(new Vec(Random.floatVal(1), Random.floatVal(1))));
		}
		spin = Random.floatVal(-5, 5);
	}
	
	public void update() {
		move();
		rotation += spin;
		wrapPosition();
	}
	
	public void render() {
		Draw.ellipse(Color.BROWN, Vec.ZERO, radius, 10);
	}
	
	public void split() {
		if(radius == FULL) {
			Asteroid a = new Asteroid(HALF), b = new Asteroid(HALF);
			a.position.set(position.sum(new Vec(20, 20)));
			b.position.set(position.sum(new Vec(-20, -20)));
			a.rotation = velocity.ang() + 10;
			b.rotation = velocity.ang() - 10;
			asteroidList.addChildren(a, b);
		}
		freeSelf();
	}
	
	public float getRadius() {
		return radius;
	}
	
}

class DeathScreen extends Node {
	
	private Color color; 
	
	public void enteredTree() {
		position.z = 10;
		color = new Color(1f, 0, 0);
		Interpolator l = new Interpolator(v -> {
			color = color.aAdj(v);
		});
		l.addKeyFrames(0, 1,   2, 0);
		l.setOnEnd(this::freeSelf);
		l.start();
		addChild(l);
	}
	
	public void render()  {
		Draw.quad(color, Vec.ZERO, viewSize());
	}
	
}