package psilox.demo.basic;

import java.awt.Font;

import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.input.Input;
import psilox.math.Random;
import psilox.math.Vec;
import psilox.node.Node;
import psilox.node.ui.Container;
import psilox.node.ui.Label;
import psilox.utils.Pointer.FloatPointer;
import static psilox.input.Input.*;

public class Main extends Node {

	private Font UIFont;
	private FloatPointer number;
	
	public void enteredTree() {
		Container UI = new Container(viewSize(), new Vec(20));
		
		UIFont = new Font("Verdana", Font.PLAIN, 32);
		
		Label center = new Label(new Color(0xFF8E8EFF), UIFont, "Look at this number: %.2f", number = new FloatPointer(0));
		center.setAnchor(Anchor.MM);
		UI.center.addChild(center);
		
		addChild(UI);
	}
	
	public void update() {
		if(keyTap(SPACE)) {
			number.set(Random.floatVal(100));
		}
		
		if(buttonTap(BUTTON_LEFT)) {
			Label hi = new Label(new Color(0xFF000000 | Random.intVal(0xFFFFFF)), UIFont, "HI!");
			hi.setAnchor(Anchor.MM);
			hi.position.set(Input.position);
			addChild(hi);
		}
	}
	
	public static void main(String[] args) {
		Psilox.start(new Config("Basic", 192 * 5, 108 * 5, true), new Main());
	}
	
}
