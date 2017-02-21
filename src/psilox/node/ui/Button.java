package psilox.node.ui;

import java.awt.Font;

import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.input.Function;
import psilox.math.Vec;
import psilox.node.Anchor;
import psilox.node.Node;

public class Button extends Panel {
	
	private Label label;
	private Color backgroundColor;
	private Color foregroundColor;
	private Color textColor;
	private Color pressedTextColor;
	private Node center;
	
	public Button(String tag, Vec dimensions, Anchor anchor, String label, Function action) {
		this(tag, dimensions, anchor, label, new Color(200, 75, 255), new Color(100, 55, 155), Color.WHITE, Color.BLACK, new Font("Verdana", Font.PLAIN, 24), action);
	}
	
	public Button(String tag, Vec dimensions, Anchor anchor, String label, Color f, Color b, Color t, Color pt, Font font, Function a) {
		super(tag, dimensions, anchor);
		this.label = new Label(null, Anchor.CENTER, textColor, font, label);
		this.label.setLayer(1);
		this.backgroundColor = b;
		this.foregroundColor = f;
		this.textColor = t;
		this.pressedTextColor = pt;
		setOnReleased(a);
	}
	
	public void enteredTree() {
		addChild(center = new Node());
		center.pos().add(getDimensions().scl(.5f));
		center.setLayer(1);
		center.setVisible(false);
		center.addChild(label);
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
		Draw.pushTransform(center.transform());
		Draw.pushTransform(label.transform());
		label.render();
		Draw.popTransform();
		Draw.popTransform();
	}
	
	public Label getLabel() {
		return label;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public Color getForegroundColor() {
		return foregroundColor;
	}

	public Color getTextColor() {
		return textColor;
	}

	public Color getPressedTextColor() {
		return pressedTextColor;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public void setPressedTextColor(Color pressedTextColor) {
		this.pressedTextColor = pressedTextColor;
	}
	
}
