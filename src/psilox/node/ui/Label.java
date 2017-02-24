package psilox.node.ui;

import java.awt.Font;

import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.graphics.Texture;
import psilox.math.Vec;
import psilox.utils.Pointer;
import psilox.utils.Pointer.PointerUpdateListener;
import psilox.utils.Pointer.StringPointer;

public class Label extends Panel implements PointerUpdateListener {

	private String prefix;
	private Pointer text;
	private Color color;
	private Font font;
	private Texture label;
	
	public Label(Color color, Font font, String text) {
		this(color, font, "%s", new StringPointer(text));
	}
	
	public Label(Color color, Font font, String prefix, Pointer text) {
		super(new Vec(0));
		this.color = color;
		this.font = font;
		this.prefix = prefix;
		this.text = text;
		text.subscribe(this);
		label = new Texture(1, 1);
		refresh();
	}
	
	public void render() {
		Draw.texture(label, Vec.ZERO, color);
	}
	
	public String getText() {
		return String.format(prefix, text.get().toString());
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
		refresh();
	}
	
	public void changed(Pointer p) {
		refresh();
	}
	
	private void refresh() {
		Draw.text(Color.WHITE, font, label, getText());
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
		refresh();
	}
	
}
