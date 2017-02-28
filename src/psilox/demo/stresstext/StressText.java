package psilox.demo.stresstext;

import java.awt.Font;

import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.math.Random;
import psilox.math.Vec;
import psilox.node.Node;
import psilox.node.ui.Container;
import psilox.node.ui.Label;
import psilox.utils.Pointer.IntPointer;

public class StressText extends Node {
	
	private Font font;
	
	private IntPointer[] pointers = new IntPointer[9];
	
	public void enteredTree() {
		font = new Font("Arial", Font.PLAIN, 48);
		
		Container c = new Container(viewSize(), new Vec(150));
		
		for(int i = 0; i < 9; i++) {
			pointers[i] = new IntPointer(0);
			Label l = new Label(Color.WHITE, font, "%s", pointers[i]);
			l.setAnchor(Anchor.MM);
			c.anchors[i].addChild(l);
		}
		
		addChild(c);
	}
	
	public void update() {
		for(IntPointer i : pointers) {
			i.set(Random.intVal());
		}
	}
	
	public static void main(String[] args) {
		Config config = new Config("StressText", 1280, 720, false);
		config.updateRate = 60;
		Psilox.start(config, new StressText());
	}
	
}
