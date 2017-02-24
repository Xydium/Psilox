package psilox.demo.asteroids;

import static psilox.input.Input.*;

import java.awt.Font;

import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.graphics.Shader;
import psilox.math.Random;
import psilox.math.Vec;
import psilox.node.Node;
import psilox.node.ui.Container;
import psilox.node.ui.Label;
import psilox.utils.Pointer.IntPointer;

public class AsteroidsDemo {

	public static void main(String[] args) {
		Psilox.start(new Config("Asteroids", 1280, 720, false), new Game());
	}
	
}

class Game extends Node {
	
	private Container UI;
	private Sky sky;
	private Player player;
	
	private IntPointer score;
	
	public void enteredTree() {
		UI = new Container(viewSize(), new Vec(10));
		UI.position.z = 2;
		
		sky = new Sky();
		sky.position.z = -1;
		
		player = new Player();
		player.position.set(viewSize().scl(.5f));
		
		score = new IntPointer(0);
		
		Label label = new Label(Color.WHITE, new Font("Verdana", Font.PLAIN, 32), "Score: ", score);
		label.setAnchor(Anchor.TL);
		UI.topLeft.addChild(label);
		
		Label quit = new Label(new Color(255, 155, 155), new Font("Verdana", Font.BOLD, 48), "Press CTRL-SHIFT-Z to Quit") {
			private float life;
			
			public void update() {
				life += Psilox.deltaTime();
				
				if(life > 4) {
					if(life > 5) {
						freeSelf();
					} else {
						setColor(getColor().aAdj(1.0f - (life - 4)));
					}
				}
			}
		};
		quit.setAnchor(Anchor.MM);
		UI.center.addChild(quit);
		
		addChildren(sky, player, UI);
	}
	
	public void update() {
		score.add(1);
	}
	
}

class Sky extends Node {
	
	private Shader sky;
	
	public void enteredTree() {
		sky = new Shader("shaders/sky.shd");
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
	
	private Vec velocity = new Vec(0);
	
	public void accelerate(float amount, float maxSpeed) {
		velocity = velocity.sum(ACCELERATION.rot(rotation).scl(amount)).clm(10);
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
	
	public void update() {
		if(accelerating = keyDown(W)) {
			accelerate(ACCELERATION, MAX_SPEED);
		}
		
		if     (keyDown(A)) rotation += ROTATION;
		else if(keyDown(D)) rotation -= ROTATION;
		
		move();
		wrapPosition();
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
