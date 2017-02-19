package psilox.node.ui;

import java.awt.Font;

import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.input.Function;
import psilox.math.Vec;
import psilox.node.Anchor;

public class Button extends Panel {
	
	private Label label;
	private Color backgroundColor;
	private Color foregroundColor;
	private Color textColor;
	private Color pressedTextColor;
	
	public Button(String tag, Vec dimensions, Anchor anchor, String label, Function action) {
		this(tag, dimensions, anchor, label, new Color(200, 75, 255), new Color(100, 55, 155), Color.WHITE, Color.BLACK, new Font("Verdana", Font.PLAIN, 24), action);
	}
	
	public Button(String tag, Vec dimensions, Anchor anchor, String label, Color f, Color b, Color t, Color pt, Font font, Function a) {
		super(tag, dimensions, anchor);
		this.label = new Label(null, Anchor.CENTER, textColor, font, label);
		this.label.pos().add(getDimensions().scl(.5f));
		this.label.setLayer(1);
		this.label.setVisible(false);
		this.backgroundColor = b;
		this.foregroundColor = f;
		this.textColor = t;
		this.pressedTextColor = pt;
		setOnReleased(a);
	}
	
	public void enteredTree() {
		addChild(label);
	}
	
	public void render() {
		Color[] colors;
		if(isPressed()) {
			colors = new Color[] { foregroundColor, foregroundColor, backgroundColor, backgroundColor };
			label.setColor(pressedTextColor);
		} else {
			colors = new Color[] { backgroundColor, backgroundColor, foregroundColor, foregroundColor };
			label.setColor(textColor);
		}
		Draw.immediate(7, colors, new Vec[] { 
				new Vec(0), new Vec(getDimensions().x, 0), getDimensions(), new Vec(0, getDimensions().y) 
		});
		Draw.pushTransform(label.transform());
		label.render();
		Draw.popTransform();
	}
	
}
