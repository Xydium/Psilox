package psilox.demo.shaders;

import java.awt.Font;

import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.graphics.Texture;
import psilox.math.IntArray2D;
import psilox.math.Vec;
import psilox.node.Node;
import psilox.node.ui.Label;
import psilox.utils.Pointer.IntPointer;

public class TextureWrite extends Node {

	private int resDiv = 1;
	private Texture tex;
	private IntArray2D data;
	private IntPointer fr;
	
	public void enteredTree() {
		tex = new Texture((int) viewSize().x / resDiv, (int) viewSize().y / resDiv);
		data = new IntArray2D(tex.getWidth(), tex.getHeight());
		
		Label l;
		getParent().addChild(l = new Label(Color.WHITE, new Font("Arial", Font.PLAIN, 16), "Framerate: %s", fr = new IntPointer(0)));
		l.setAnchor(Anchor.TL);
		l.position.set(0, viewSize().y, 10);
	}
	
	public void update() {
		data.iterate((x, y) -> {
			return 0xFF000000 | ((x % (y + 1)) ^ (int) Psilox.ticks());
		});
		
		tex.setData(data.array, data.width, data.height);
	}
	
	public void render() {
		fr.set((int) (1 / Psilox.updateTime));
		
		Draw.texture(tex, Vec.ZERO, viewSize(), Color.WHITE);
	}
	
	public static void main(String[] args) {
		Psilox.start(new Config("TextureWrite", 1280, 720, false), new TextureWrite());
	}
	
}
