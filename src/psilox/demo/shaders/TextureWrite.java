package psilox.demo.shaders;

import java.awt.Font;

import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.graphics.Texture;
import psilox.input.Input;
import psilox.math.IntArray2D;
import psilox.math.Random;
import psilox.math.Vec;
import psilox.node.Node;
import psilox.node.ui.Label;
import psilox.utils.Pointer.IntPointer;

public class TextureWrite extends Node {
	
	private Texture tex;
	private IntArray2D data;
	private IntPointer fr;
	
	public void enteredTree() {
		tex = new Texture(viewSize());
		data = new IntArray2D(tex.getDimensions());
		
		Label l = new Label(Color.BLACK, new Font("Arial", Font.PLAIN, 16), "Framerate:\nLol\nLol\n%s", fr = new IntPointer(0));
		l.setAnchor(Anchor.MM);
		l.position.set(viewSize().scl(.5f).sum(new Vec(0, 0, 1)));
		l.setBg(Color.WHITE);
		getParent().addChild(l);
		
		data.iterate((i, v) -> {
			return 0xFF000000 | Random.intVal(0xFFFFFF);
		});
		
		tex.setData(data.array, data.width, data.height);
	}
	
	public void update() {
		if(Input.keyDown(Input.SPACE)) {
			data.iterate((x, y, v) -> {
				int[][] area = data.getArea(x - 1, y - 1, 3, 3, 0xFF000000);
				long sum = 0;
				for(int xx = 0; xx < 3; xx++) {
					for(int yy = 0; yy < 3; yy++) {
						sum += area[xx][yy];
					}
				}
				return (int) (sum / 9);
			});
			
			tex.setData(data.array, data.width, data.height);
		}
	}
	
	public void render() {
		fr.set((int) (1 / Psilox.updateTime));
		Draw.texture(tex, Vec.ZERO, viewSize(), Color.WHITE);
	}
	
	public static void main(String[] args) {
		Psilox.start(new Config("TextureWrite", 720, 360, false), new TextureWrite());
	}
	
}