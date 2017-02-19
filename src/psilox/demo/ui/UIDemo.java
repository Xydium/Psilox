package psilox.demo.ui;

import java.awt.Font;

import psilox.core.Config;
import psilox.core.Psilox;
import psilox.graphics.Color;
import psilox.input.Input;
import psilox.input.InputEvent;
import psilox.input.InputEvent.InputState;
import psilox.math.Vec;
import psilox.node.Anchor;
import psilox.node.Node;
import psilox.node.ui.Container;
import psilox.node.ui.Label;

public class UIDemo extends Node {
	
	private static final Font f = new Font("Arial", Font.PLAIN, 36);
	
	private Container container;
	private Label label;
	private StringBuilder text;
	
	public void enteredTree() {
		setInputListening(true);
		addChild(container = new Container("container", viewSize(), new Vec(50)));
		container.center.addChild(label = new Label("label", Anchor.CENTER, Color.WHITE, f, ""));
		text = new StringBuilder();
	}
	
	public void receiveInput(InputEvent ev) {
		if(ev.state == InputState.CHARACTER) {
			text.append((char) ev.key);
			label.setText(text.toString());
		} else if (ev.state == InputState.PRESSED || ev.state == InputState.REPEAT) {
			if(ev.key == Input.BACKSPACE && text.length() > 0) {
				text.deleteCharAt(text.length() - 1);
				label.setText(text.toString());
			}
		}
	}
	
	public static void main(String[] args) {
		new Psilox(new Config("UIDemo", 600, 400, false)).start(new UIDemo());
	}
	
}