package psilox.node.ui;

import java.awt.Font;

import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.graphics.Texture;
import psilox.math.Vec;
import psilox.node.Anchor;

public class Label extends Panel {

	private String text;
	private Color color;
	private Font font;
	private Texture label;
	
	public Label(String tag, Anchor anchor, Color color, Font font, String text) {
		super(tag, Vec.ZERO, anchor);
		this.color = color;
		this.font = font;
		label = new Texture(1, 1);
		setText(text);
	}
	
	public void render() {
		Draw.texture(label, Vec.ZERO, color);
	}
	
	public void setText(String text) {
		this.text = text;
		Draw.text(Color.WHITE, font, label, text);
		setDimensions(new Vec(label.getWidth(), label.getHeight()));
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
		setText(getText());
	}

	public Texture getLabel() {
		return label;
	}

	public void setLabel(Texture label) {
		this.label = label;
	}

	public String getText() {
		return text;
	}
	
}
