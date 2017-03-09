package psilox.demo.shaders;

import java.awt.Font;

import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.math.Vec;
import psilox.node.Node;
import psilox.node.ui.TextField;

public class TextFieldTest extends Node {

	public void enteredTree() {
		TextField f = new TextField(Color.BLACK, Color.WHITE, new Font("Verdana", Font.PLAIN, 24), new Vec(500, 30), 42);
		f.position.set(20, 20);
		addChild(f);
	}
	
	public static void main(String[] args) {
		Psilox.start(new Config("TextField Test", 1280, 720, false), new TextFieldTest());
	}
	
}
