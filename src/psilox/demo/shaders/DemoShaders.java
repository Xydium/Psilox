package psilox.demo.shaders;

import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.graphics.Shader;
import psilox.math.Vec;
import psilox.node.Node;

public class DemoShaders extends Node {

	private Shader shader;
	
	public void enteredTree() {
		shader = new Shader("psilox/demo/shaders/demo.shd");
		shader.setUniform4f("color", Color.WHITE);
	}
	
	public void render() {
		shader.enable();
		float time = Psilox.ticks() / 10f;
		shader.setUniform1f("time", time);
		time = (float) Math.sin(time) + 1;
		Draw.quad(Color.WHITE, new Vec(time * 100, time * 100), viewSize().dif(new Vec(time * 200, time * 200)));
		shader.disable();
	}
	
	public static void main(String[] args) {
		Psilox.start(new Config("Demo Shaders", 1280, 720, false), new DemoShaders());
	}
	
}
