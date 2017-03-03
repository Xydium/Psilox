package psilox.demo.shaders;

import static psilox.math.Mathf.*;
import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.graphics.Shader;
import psilox.graphics.Shape;
import psilox.math.Vec;
import psilox.node.Node;

public class DemoShaders extends Node {

	private Shape shape;
	private Shader shader;
	
	public void enteredTree() {
		float x = viewSize().x, y = viewSize().y;
		
		shape = new Shape(Shape.QUAD, new Vec[] { new Vec(0, 0), new Vec(x, 0), new Vec(x, y), new Vec(0, y)}, Color.WHITE,
				new byte[] {0, 1, 2, 3});
		
		shader = new Shader("psilox/demo/shaders/demo.shd");
		shader.setUniform4f("color", Color.WHITE);
	}
	
	public void render() {
		shader.enable();
		float time = Psilox.ticks() / 10f;
		shader.setUniform1f("time", time);
		time = sin(time) + 1;
		Draw.shape(shape);
		shader.disable();
	}
	
	public static void main(String[] args) {
		Psilox.start(new Config("Demo Shaders", 1280, 720, false), new DemoShaders());
	}
	
}
