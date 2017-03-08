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
		
		Label l = new Label(Color.BLACK, new Font("Arial", Font.PLAIN, 16), "Framerate:%s", fr = new IntPointer(0));
		l.setAnchor(Anchor.TL);
		l.position.set(0, viewSize().y, 1);
		l.setBg(Color.WHITE);
		getParent().addChild(l);
		
		data.iterate((i, v) -> {
			return 0xFF000000 | Random.intVal(0xFFFFFF);
		});
		
		tex.setData(data.array, data.width, data.height);
	}
	
	public void update() {
		if(Input.keyDown(Input.SPACE)) {
			int[] result = new int[data.array.length];
			data.iterate((x, y, v) -> {
				int[][] area = data.getArea(x - 1, y - 1, 3, 3, -1);
				int sum = v;
				for(int xx = 0; xx < 3; xx++) {
					for(int yy = 0; yy < 3; yy++) {
						int b = area[xx][yy];
						if(b == -1) continue;
						sum = (((sum ^ b) & 0xfefefefe) >> 1) + (sum & b);
					}
				}
				result[y * data.width + x] = sum;
				return v;
			});
			data = new IntArray2D(data.width, data.height, result);
			
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