package psilox.node.ui;

import java.awt.Font;

import psilox.graphics.Color;
import psilox.graphics.Draw;
import psilox.graphics.Texture;
import psilox.input.Input;
import psilox.input.InputEvent;
import psilox.input.InputEvent.InputState;
import psilox.math.Vec;

public class TextField extends Panel {

	private StringBuilder content;
	private Color color;
	private Color bg;
	private Font font;
	private Texture label;
	private int cposition;
	private int dposition;
	private int advanceDistance;
	
	public TextField(Color color, Color bg, Font font, Vec dimensions, int advanceDistance) {
		super(dimensions);
		this.content = new StringBuilder();
		this.color = color;
		this.bg = bg;
		this.font = font;
		this.label = new Texture(dimensions);
		this.cposition = 0;
		this.dposition = 0;
		this.advanceDistance = advanceDistance;
	}
	
	public void render() {
		if(hasFocus()) {
			if(Math.abs(cposition - dposition) > advanceDistance) {
				dposition = cposition - advanceDistance;
				if(dposition < 0) dposition = 0;
			}
			if(dposition > content.length()) dposition = content.length();
			Draw.text(color, font, label, getDimensions(), getText().substring(dposition));
		}
		Draw.quad(bg, Vec.ZERO, getDimensions());
		Draw.texture(label, new Vec(0, 0, .1f), color);
	}
	
	public String getText() {
		return content.toString();
	}
	
	public void receiveInput(InputEvent ev) {
		super.receiveInput(ev);
		
		if((ev.state == InputState.PRESSED || ev.state == InputState.REPEAT)) {
			switch(ev.key) {
			case Input.LEFT:
				cposition--;
				if(cposition < 0) cposition = 0;
				break;
			case Input.RIGHT:
				cposition++;
				if(cposition > content.length()) cposition = content.length();
				break;
			case Input.HOME:
				cposition = 0;
				break;
			case Input.END:
				cposition = content.length();
				break;
			case Input.BACKSPACE:
				if(content.length() == 0) break;
				content.deleteCharAt(cposition - 1);
				cposition--;
				break;
			}
		} else if(ev.state == InputState.CHARACTER) {
			if(cposition < content.length()) {
				content.insert(cposition, (char) ev.key);
			} else {
				content.append((char) ev.key);
			}
			cposition++;
		}
	}
	
}
