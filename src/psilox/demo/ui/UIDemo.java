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
import psilox.node.ui.Button;
import psilox.node.ui.Container;
import psilox.node.ui.Label;

public class UIDemo extends Node {
	
	private static final Font f = new Font("Arial", Font.PLAIN, 18);
	
	private Container container;
	private Label label;
	private StringBuilder text;
	private Button a, b;
	
	public void enteredTree() {
		setInputListening(true);
		addChild(container = new Container("container", viewSize(), new Vec(50)));
		container.center.addChild(label = new Label("label", Anchor.CENTER, Color.WHITE, f, ""));
		text = new StringBuilder();
		container.topLeft.addChild(a = new Button(null, new Vec(200, 50), Anchor.TOP_LEFT, "Button One", () -> { label.setText("l33t sk1llz"); }));
		container.topLeft.addChild(b = new Button(null, new Vec(200, 50), Anchor.TOP_LEFT, "Button Two", () -> { label.setText("thanks"); }));
		b.pos().sub(new Vec(0, 60));
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
		Psilox.start(new Config("UIDemo", 1280, 720, false), new UIDemo());
	}
	
}