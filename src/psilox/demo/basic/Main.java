package psilox.demo.basic;

import java.awt.Font;

import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.math.Random;
import psilox.math.Vec;
import psilox.node.Node;
import psilox.node.ui.Container;
import psilox.node.ui.Label;
import psilox.utils.Pointer.FloatPointer;

public class Main extends Node {

	private Font UIFont;
	private FloatPointer number;
	
	public void enteredTree() {
		Container UI = new Container(viewSize(), new Vec(20));
		
		UIFont = new Font("Verdana", Font.PLAIN, 16);
		
		Label center = new Label(new Color(0xFF8E8EFF), UIFont, "Look at this number: %.2f", number = new FloatPointer(0));
		center.setAnchor(Anchor.MM);
		UI.center.addChild(center);
		
		addChild(UI);
	}
	
	public void update() {
		number.set(Random.floatVal(100));
	}
	
	public static void main(String[] args) {
		Psilox.start(new Config("Basic", 1280, 720, false), new Main());
	}
	
}
