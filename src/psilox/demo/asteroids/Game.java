package psilox.demo.asteroids;

import psilox.core.Config;
import psilox.core.Node;
import psilox.core.Psilox;
import psilox.math.Vec;

public class Game extends Node {

	private Sky sky;
	private Player player;
	
	public void added() {
		addChild(sky = new Sky());
		sky.transform().translate(new Vec(0, 0, -1));
		
		addChild(player = new Player());
		player.transform().translate(viewSize().scl(.5f));
	}
	
	public static void main(String[] args) {
		Config c = new Config();
		c.fullscreen = true;
		c.width = 1920;
		c.height = 1080;
		new Psilox(c).start(new Game());
	}
	
}
