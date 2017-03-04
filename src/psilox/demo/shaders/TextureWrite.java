package psilox.demo.shaders;

import java.awt.Font;

import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.graphics.Shader;
import psilox.math.Vec;
import psilox.node.Node;
import psilox.node.ui.Label;
import psilox.utils.Pointer.IntPointer;

/*
public class TextureWrite extends Node {
	
	private Texture tex;
	private IntArray2D data;
	private IntPointer fr;
	
	public void enteredTree() {
		tex = new Texture((int) viewSize().x, (int) viewSize().y);
		data = new IntArray2D(tex.getWidth(), tex.getHeight());
		
		Label l;
		getParent().addChild(l = new Label(Color.WHITE, new Font("Arial", Font.PLAIN, 16), "Framerate: %s", fr = new IntPointer(0)));
		l.setAnchor(Anchor.TL);
		l.position.set(2, viewSize().y, 10);
	}
	
	public void update() {
		data.iterate((x, y, v) -> {
			return 0xFF000000 | ((x % (y + 1)) ^ (int) Psilox.ticks());
		});
		
		tex.setData(data.array, data.width, data.height);
	}
	
	public void render() {
		fr.set((int) (1 / Psilox.updateTime));
		
		Draw.texture(tex, Vec.ZERO, viewSize(), Color.WHITE);
	}
	
	public static void main(String[] args) {
		Psilox.start(new Config("TextureWrite", 1600, 900, false), new TextureWrite());
	}
	
}
*/

public class TextureWrite extends Node {
	
	private Shader shader;
	private IntPointer fr;
	
	public void enteredTree() {
		shader = new Shader("psilox/demo/shaders/write.shd");
		
		Label l;
		getParent().addChild(l = new Label(Color.WHITE, new Font("Arial", Font.PLAIN, 16), "Framerate: %s", fr = new IntPointer(0)));
		l.setAnchor(Anchor.TL);
		l.position.set(2, viewSize().y, 10);
	}
	
	public void render() {
		fr.set((int) (1 / Psilox.renderTime));
		shader.enable();
		shader.setUniform1f("time", (int) Psilox.ticks());
		Draw.quad(Color.WHITE, Vec.ZERO, viewSize());
		shader.disable();
	}
	
	public static void main(String[] args) {
		Psilox.start(new Config("TextureWrite", 400, 800, false), new TextureWrite());
	}
	
}
