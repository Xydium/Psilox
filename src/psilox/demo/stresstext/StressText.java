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

	private static final Font FONT = new Font("Arial", Font.PLAIN, 16);
	
	private IntPointer[] labelPointers = new IntPointer[9 * 4];
	
	public void enteredTree() {
		Vec[] ofs = new Vec[] { new Vec(0), new Vec(640, 0), new Vec(0, 360), new Vec(640, 360) };
		for(int k = 0; k < 4; k++) {
			Container c = new Container(viewSize().scl(.5f), new Vec(100));
			c.position.add(ofs[k]);
			
			for(int i = 0; i < 9; i++) {
				Label l = new Label(Color.WHITE, FONT, "%s", labelPointers[i + k * 9] = new IntPointer(0));
				l.setAnchor(Anchor.MM);
				c.anchors[i].addChild(l);
			}
			
			addChild(c);
		}
	}
	
	public void update() {
		for(IntPointer i : labelPointers) {
			i.set(Random.intVal());
		}
	}
	
	public static void main(String[] args) {
		Psilox.start(new Config("StressText", 1280, 720, false), new StressText());
	}
	
}
