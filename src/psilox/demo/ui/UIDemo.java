package psilox.demo.ui;

import java.awt.Font;

import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.math.Vec;
import psilox.node.Anchor;
import psilox.node.Node;
import psilox.node.ui.Container;
import psilox.node.ui.Label;

public class UIDemo extends Node {
	
	private static final Font f = new Font("Arial", Font.PLAIN, 36);
	
	private Container container;
	
	public void enteredTree() {
		addChild(container = new Container("container", viewSize(), new Vec(100)));
		
		for(int i = 0; i < 9; i++) {
			container.anchors[i].addChild(new Label(null, Anchor.values()[i], Color.WHITE, f, "KEK") {
				public void pressed() {
					setColor(Color.GREEN);
				}
				
				public void released() {
					setColor(Color.RED);
					setText(getText().equals("PEPE") ? "KEK" : "PEPE");
				}
				
				public void mouseEntered() {
					setColor(Color.RED);
				}
				
				public void mouseExited() {
					setColor(Color.WHITE);
				}
			});
		}
	}
	
	public static void main(String[] args) {
		new Psilox(new Config("UIDemo", 600, 400, false)).start(new UIDemo());
	}
	
}